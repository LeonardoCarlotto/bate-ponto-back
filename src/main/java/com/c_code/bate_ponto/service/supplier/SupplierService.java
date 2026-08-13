package com.c_code.bate_ponto.service.supplier;

import com.c_code.bate_ponto.dto.request.SupplierAddressRequest;
import com.c_code.bate_ponto.dto.request.SupplierContactRequest;
import com.c_code.bate_ponto.dto.request.SupplierRequest;
import com.c_code.bate_ponto.model.Supplier;
import com.c_code.bate_ponto.model.SupplierAddress;
import com.c_code.bate_ponto.model.SupplierContact;
import com.c_code.bate_ponto.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier createSupplier(SupplierRequest request) {
        validateRequest(request, null);

        if (supplierRepository.existsByCnpj(request.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }

        Supplier supplier = new Supplier(
            trim(request.getNome()),
            trim(request.getCnpj()),
            trim(request.getEmail()),
            trim(request.getTelefone()),
            trim(request.getInscricaoEstadual())
        );
        supplier.setActive(request.getAtivo() == null || request.getAtivo());
        syncPrimaryContact(supplier, request.getContato(), request.getEmail(), request.getTelefone());
        syncPrimaryAddress(supplier, request.getEndereco(), request.getCidade(), request.getEstado(), request.getCep());

        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        validateRequest(request, id);

        supplier.setName(trim(request.getNome()));
        supplier.setCnpj(trim(request.getCnpj()));
        supplier.setEmail(trim(request.getEmail()));
        supplier.setPhone(trim(request.getTelefone()));
        supplier.setStateRegistration(trim(request.getInscricaoEstadual()));
        supplier.setActive(request.getAtivo() == null || request.getAtivo());
        syncPrimaryContact(supplier, request.getContato(), request.getEmail(), request.getTelefone());
        syncPrimaryAddress(supplier, request.getEndereco(), request.getCidade(), request.getEstado(), request.getCep());

        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<Supplier> listSuppliers(String nome) {
        List<Supplier> suppliers = nome != null && !nome.trim().isEmpty()
            ? supplierRepository.findByFilters(nome)
            : supplierRepository.findAll();
        suppliers.forEach(this::initializeChildren);
        return suppliers;
    }

    @Transactional(readOnly = true)
    public Supplier findById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        initializeChildren(supplier);
        return supplier;
    }

    public List<Supplier> findByFilters(String nome) {
        return supplierRepository.findByFilters(nome);
    }

    @Transactional(readOnly = true)
    public Supplier findByCnpj(String cnpj) {
        Supplier supplier = supplierRepository.findByCnpj(cnpj)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        initializeChildren(supplier);
        return supplier;
    }

    @Transactional(readOnly = true)
    public List<SupplierContact> listContacts(Long supplierId) {
        Supplier supplier = findById(supplierId);
        return supplier.getContacts();
    }

    @Transactional
    public SupplierContact addContact(Long supplierId, SupplierContactRequest request) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        String nome = trim(request.getNome());
        String telefone = trim(request.getTelefone());
        String email = trim(request.getEmail());
        if (isBlank(nome) && isBlank(telefone) && isBlank(email)) {
            throw new RuntimeException("Informe nome, telefone ou email do contato");
        }

        SupplierContact contact = new SupplierContact(supplier, nome, trim(request.getCargo()), email, telefone);
        supplier.getContacts().add(contact);
        supplierRepository.save(supplier);
        return contact;
    }

    @Transactional
    public void removeContact(Long supplierId, Long contactId) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        boolean removed = supplier.getContacts().removeIf(contact -> contact.getId().equals(contactId));
        if (!removed) {
            throw new RuntimeException("Contato não encontrado");
        }
        supplierRepository.save(supplier);
    }

    @Transactional(readOnly = true)
    public List<SupplierAddress> listAddresses(Long supplierId) {
        Supplier supplier = findById(supplierId);
        return supplier.getAddresses();
    }

    @Transactional
    public SupplierAddress addAddress(Long supplierId, SupplierAddressRequest request) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        if (isBlank(request.getLogradouro()) && isBlank(request.getCidade()) && isBlank(request.getCep())) {
            throw new RuntimeException("Informe endereço, cidade ou CEP");
        }

        SupplierAddress address = new SupplierAddress(
            supplier,
            trim(request.getLogradouro()),
            trim(request.getNumero()),
            trim(request.getBairro()),
            trim(request.getCidade()),
            trim(request.getEstado()),
            trim(request.getCep()),
            trim(request.getTipo())
        );
        supplier.getAddresses().add(address);
        supplierRepository.save(supplier);
        return address;
    }

    @Transactional
    public void removeAddress(Long supplierId, Long addressId) {
        Supplier supplier = supplierRepository.findById(supplierId)
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        boolean removed = supplier.getAddresses().removeIf(address -> address.getId().equals(addressId));
        if (!removed) {
            throw new RuntimeException("Endereço não encontrado");
        }
        supplierRepository.save(supplier);
    }

    private void validateRequest(SupplierRequest request, Long currentId) {
        if (request == null) {
            throw new RuntimeException("Dados do fornecedor são obrigatórios");
        }
        if (isBlank(request.getNome())) {
            throw new RuntimeException("Nome do fornecedor é obrigatório");
        }
        if (isBlank(request.getCnpj())) {
            throw new RuntimeException("CNPJ é obrigatório");
        }
        if (!isBlank(request.getEmail()) && !request.getEmail().contains("@")) {
            throw new RuntimeException("Email inválido");
        }
        if (currentId != null && supplierRepository.existsByCnpjAndIdNot(request.getCnpj(), currentId)) {
            throw new RuntimeException("CNPJ já cadastrado para outro fornecedor");
        }
    }

    private void syncPrimaryContact(Supplier supplier, String nome, String email, String telefone) {
        if (isBlank(nome) && isBlank(email) && isBlank(telefone)) {
            supplier.getContacts().clear();
            return;
        }

        SupplierContact contact = supplier.getContacts().isEmpty()
            ? new SupplierContact(supplier, null, null, null, null)
            : supplier.getContacts().get(0);

        contact.setSupplier(supplier);
        contact.setName(trim(nome));
        contact.setEmail(trim(email));
        contact.setPhone(trim(telefone));

        if (supplier.getContacts().isEmpty()) {
            supplier.getContacts().add(contact);
        }
    }

    private void syncPrimaryAddress(Supplier supplier, String endereco, String cidade, String estado, String cep) {
        if (isBlank(endereco) && isBlank(cidade) && isBlank(estado) && isBlank(cep)) {
            supplier.getAddresses().clear();
            return;
        }

        SupplierAddress address = supplier.getAddresses().isEmpty()
            ? new SupplierAddress(supplier, null, null, null, null, null, null, "COMERCIAL")
            : supplier.getAddresses().get(0);

        address.setSupplier(supplier);
        address.setStreet(trim(endereco));
        address.setCity(trim(cidade));
        address.setState(trim(estado));
        address.setZipCode(trim(cep));
        address.setType("COMERCIAL");

        if (supplier.getAddresses().isEmpty()) {
            supplier.getAddresses().add(address);
        }
    }

    private void initializeChildren(Supplier supplier) {
        supplier.getContacts().size();
        supplier.getAddresses().size();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
