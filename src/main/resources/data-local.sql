ALTER TABLE users ALTER COLUMN url_photo TYPE TEXT;

MERGE INTO users (id, name, email, type, password, role, active, url_photo)
KEY(email)
VALUES (
  1,
  'Administrador',
  'admin@bateponto.com',
  'ADMIN',
  '$2a$12$6qN8uZ7jWb7nR1uk10EfxO2uqti3m.30iP0NB7DMzOv/BgbMrY6du',
  'ADMIN',
  true,
  null
);

MERGE INTO users (id, name, email, type, password, role, active, url_photo)
KEY(email)
VALUES
(1001, 'Operador Teste', 'operador@bateponto.com', 'EMPLOYEE', '$2a$12$6qN8uZ7jWb7nR1uk10EfxO2uqti3m.30iP0NB7DMzOv/BgbMrY6du', 'EMPLOYEE', true, null);

MERGE INTO categories (id, name, description)
KEY(id)
VALUES
(1001, 'Arquivo Fisico', 'Produtos fotograficos impressos'),
(1002, 'Decoracao', 'Quadros, porta-retratos e itens decorativos'),
(1003, 'Digital', 'Produtos e entregas digitais'),
(1004, 'Acessorios', 'Acessorios e complementos para ensaios');

MERGE INTO products (id, active, description, name, price, stock, category_id)
KEY(id)
VALUES
(1001, true, 'Revelacao fotografica 10x15 em papel brilho', 'Foto 10x15', 18.00, 500, 1001),
(1002, true, 'Quadro fotografico 15x21 com moldura preta', 'Quadro 15x21', 39.90, 80, 1002),
(1003, true, 'Album pequeno com 20 fotos impressas', 'Album Mini 20 Fotos', 129.90, 25, 1001),
(1004, true, 'Arquivo digital tratado em alta resolucao', 'Arquivo Digital Tratado', 24.90, 999, 1003),
(1005, true, 'Fotolivro premium com capa dura', 'Fotolivro Premium', 249.90, 15, 1001),
(1006, true, 'Porta-retrato de mesa 10x15', 'Porta-retrato 10x15', 32.50, 40, 1002),
(1007, true, 'Pen drive personalizado para entrega de fotos', 'Pen Drive Personalizado', 59.90, 35, 1003),
(1008, false, 'Produto inativo para testar filtros', 'Moldura Antiga', 19.90, 0, 1002);

MERGE INTO services (id, active, description, name, price)
KEY(id)
VALUES
(1001, true, 'Sessao fotografica em estudio com direcao basica', 'Ensaio em Estudio', 350.00),
(1002, true, 'Cobertura externa de ate duas horas', 'Ensaio Externo', 480.00),
(1003, true, 'Tratamento profissional por foto adicional', 'Edicao Avancada', 35.00),
(1004, true, 'Diagramacao de album ou fotolivro', 'Diagramacao de Album', 180.00);

MERGE INTO packages (id, active, data_cadastro, description, duration_days, name, price)
KEY(id)
VALUES
(1001, true, CURRENT_TIMESTAMP, 'Pacote para ensaio rapido com impressos e digitais', 15, 'Ensaio Essencial', 499.90),
(1002, true, CURRENT_TIMESTAMP, 'Pacote familia com album, quadros e arquivos digitais', 30, 'Familia Completo', 1199.90),
(1003, true, CURRENT_TIMESTAMP, 'Pacote de presente com porta-retrato e fotos impressas', 7, 'Presente Fotografico', 179.90);

MERGE INTO package_products (package_id, product_id)
KEY(package_id, product_id)
VALUES
(1001, 1001),
(1001, 1004),
(1002, 1002),
(1002, 1003),
(1002, 1004),
(1002, 1005),
(1003, 1001),
(1003, 1006);

MERGE INTO clients (id, active, cpf_cnpj, data_abertura, data_aniversario, data_cadastro, email, name, telefone)
KEY(id)
VALUES
(1001, true, '11122233344', '2024-01-15', '1990-08-13', CURRENT_TIMESTAMP, 'ana.silva@example.com', 'Ana Silva', '(51) 99911-2233'),
(1002, true, '22233344455', '2024-02-10', '1988-11-02', CURRENT_TIMESTAMP, 'bruno.costa@example.com', 'Bruno Costa', '(51) 98822-3344'),
(1003, true, '33344455566', '2024-03-22', '1995-05-19', CURRENT_TIMESTAMP, 'carla.mendes@example.com', 'Carla Mendes', '(54) 97733-4455'),
(1004, true, '44455566677', '2024-04-08', '1983-12-30', CURRENT_TIMESTAMP, 'daniel.rocha@example.com', 'Daniel Rocha', '(53) 96644-5566'),
(1005, true, '55566677788', '2024-05-14', '1998-03-07', CURRENT_TIMESTAMP, 'estudio.luz@example.com', 'Estudio Luz Criativa', '(51) 3555-7788'),
(1006, false, '66677788899', '2023-12-01', '1992-07-21', CURRENT_TIMESTAMP, 'cliente.inativo@example.com', 'Cliente Inativo', '(51) 94455-6677');

MERGE INTO addresses (id, bairro, cep, cidade, complemento, estado, numero, principal, rua, client_id)
KEY(id)
VALUES
(1001, 'Centro', '90010000', 'Porto Alegre', 'Apto 302', 'RS', '120', true, 'Rua dos Andradas', 1001),
(1002, 'Moinhos de Vento', '90570020', 'Porto Alegre', null, 'RS', '455', true, 'Rua Padre Chagas', 1002),
(1003, 'Sao Pelegrino', '95020070', 'Caxias do Sul', 'Sala 4', 'RS', '88', true, 'Avenida Julio de Castilhos', 1003),
(1004, 'Centro', '96010000', 'Pelotas', null, 'RS', '230', true, 'Rua XV de Novembro', 1004),
(1005, 'Menino Deus', '90150000', 'Porto Alegre', 'Loja 2', 'RS', '780', true, 'Avenida Getulio Vargas', 1005);

MERGE INTO contacts (id, principal, tipo, valor, client_id)
KEY(id)
VALUES
(1001, true, 'CELULAR', '(51) 99911-2233', 1001),
(1002, true, 'EMAIL', 'ana.silva@example.com', 1001),
(1003, true, 'CELULAR', '(51) 98822-3344', 1002),
(1004, true, 'CELULAR', '(54) 97733-4455', 1003),
(1005, true, 'TELEFONE', '(51) 3555-7788', 1005);

MERGE INTO suppliers (id, active, cnpj, data_cadastro, email, name, phone, state_registration)
KEY(id)
VALUES
(1001, true, '11222333000144', CURRENT_TIMESTAMP, 'vendas@fotoprint.example.com', 'FotoPrint Insumos', '(51) 3333-1001', '0961234567'),
(1002, true, '22333444000155', CURRENT_TIMESTAMP, 'contato@moldurasul.example.com', 'Molduras Sul', '(51) 3333-1002', '0962345678'),
(1003, true, '33444555000166', CURRENT_TIMESTAMP, 'financeiro@papelariaalpha.example.com', 'Papelaria Alpha', '(51) 3333-1003', '0963456789'),
(1004, false, '44555666000177', CURRENT_TIMESTAMP, 'antigo@fornecedor.example.com', 'Fornecedor Inativo', '(51) 3333-1004', '0964567890');

MERGE INTO supplier_addresses (id, city, neighborhood, number, state, street, type, zip_code, supplier_id)
KEY(id)
VALUES
(1001, 'Porto Alegre', 'Navegantes', '500', 'RS', 'Rua Voluntarios da Patria', 'COMERCIAL', '90230010', 1001),
(1002, 'Canoas', 'Centro', '98', 'RS', 'Avenida Victor Barreto', 'COMERCIAL', '92010000', 1002),
(1003, 'Porto Alegre', 'Centro Historico', '745', 'RS', 'Rua Riachuelo', 'COMERCIAL', '90010010', 1003);

MERGE INTO supplier_contacts (id, email, name, phone, position, supplier_id)
KEY(id)
VALUES
(1001, 'marina@fotoprint.example.com', 'Marina Lopes', '(51) 99900-1001', 'Vendas', 1001),
(1002, 'rafael@moldurasul.example.com', 'Rafael Nunes', '(51) 99900-1002', 'Atendimento', 1002),
(1003, 'luisa@papelariaalpha.example.com', 'Luisa Prado', '(51) 99900-1003', 'Financeiro', 1003);

MERGE INTO orders (id, date, description, forma_pagamento, parcelas, status, value, client_id)
KEY(id)
VALUES
(1001, TIMESTAMP '2026-08-01 10:00:00', 'Ensaio essencial Ana Silva', 'PIX', 1, 'ENTREGUE', 499.90, 1001),
(1002, TIMESTAMP '2026-08-03 14:30:00', 'Pedido parcial Bruno Costa', 'CARTAO_CREDITO', 3, 'PREPARACAO', 1199.90, 1002),
(1003, TIMESTAMP '2026-08-06 09:15:00', 'Impressos Carla Mendes', 'DINHEIRO', 1, 'PREPARACAO', 183.00, 1003),
(1004, TIMESTAMP '2026-08-08 16:00:00', 'Pedido entregue e pago Daniel Rocha', 'PIX', 1, 'ENTREGUE', 179.90, 1004),
(1005, TIMESTAMP '2026-08-10 11:45:00', 'Pedido cancelado para teste', 'BOLETO', 1, 'CANCELADO', 249.90, 1005),
(1006, TIMESTAMP '2026-08-12 15:20:00', 'Fotolivro e arquivos digitais Estudio Luz', 'CARTAO_DEBITO', 1, 'PREPARACAO', 349.50, 1005);

MERGE INTO order_items (id, item_name, item_type, quantity, subtotal, unit_price, order_id, package_id, product_id)
KEY(id)
VALUES
(1001, 'Ensaio Essencial', 'pacote', 1, 499.90, 499.90, 1001, 1001, null),
(1002, 'Familia Completo', 'pacote', 1, 1199.90, 1199.90, 1002, 1002, null),
(1003, 'Foto 10x15', 'produto', 6, 108.00, 18.00, 1003, null, 1001),
(1004, 'Arquivo Digital Tratado', 'produto', 3, 74.70, 24.90, 1003, null, 1004),
(1005, 'Presente Fotografico', 'pacote', 1, 179.90, 179.90, 1004, 1003, null),
(1006, 'Fotolivro Premium', 'produto', 1, 249.90, 249.90, 1005, null, 1005),
(1007, 'Fotolivro Premium', 'produto', 1, 249.90, 249.90, 1006, null, 1005),
(1008, 'Arquivo Digital Tratado', 'produto', 4, 99.60, 24.90, 1006, null, 1004);

MERGE INTO contas_receber (id, cliente_id, cliente_nome, created_at, data_pagamento, descricao, forma_pagamento, pedido_id, status, tipo_pagamento, updated_at, valor)
KEY(id)
VALUES
(1001, 1001, 'Ana Silva', CURRENT_TIMESTAMP, '2026-08-01', 'Pagamento total pedido #1001', 'pix', 1001, 'PAGO', 'pedido_individual', CURRENT_TIMESTAMP, 499.90),
(1002, 1002, 'Bruno Costa', CURRENT_TIMESTAMP, '2026-08-04', 'Entrada pedido #1002', 'cartao_credito', 1002, 'PAGO', 'pedido_individual', CURRENT_TIMESTAMP, 400.00),
(1003, 1004, 'Daniel Rocha', CURRENT_TIMESTAMP, '2026-08-09', 'Pagamento total pedido #1004', 'pix', 1004, 'PAGO', 'pedido_individual', CURRENT_TIMESTAMP, 179.90),
(1004, 1005, 'Estudio Luz Criativa', CURRENT_TIMESTAMP, '2026-08-12', 'Pagamento parcial pedido #1006', 'cartao_debito', 1006, 'PAGO', 'pedido_individual', CURRENT_TIMESTAMP, 100.00),
(1005, 1003, 'Carla Mendes', CURRENT_TIMESTAMP, '2026-08-07', 'Pagamento estornado para teste', 'dinheiro', 1003, 'ESTORNADO', 'pedido_individual', CURRENT_TIMESTAMP, 50.00);

MERGE INTO contas_pagar (id, created_at, data_pagamento, data_vencimento, descricao, forma_pagamento, fornecedor_id, fornecedor_nome, parcelas, status, updated_at, valor)
KEY(id)
VALUES
(1001, CURRENT_TIMESTAMP, null, '2026-08-20', 'Reposicao de papel fotografico', 'BOLETO', 1001, 'FotoPrint Insumos', 1, 'PENDENTE', CURRENT_TIMESTAMP, 680.00),
(1002, CURRENT_TIMESTAMP, '2026-08-05', '2026-08-05', 'Compra de molduras 15x21', 'PIX', 1002, 'Molduras Sul', 1, 'PAGO', CURRENT_TIMESTAMP, 420.50),
(1003, CURRENT_TIMESTAMP, null, '2026-08-10', 'Material de escritorio vencido', 'TRANSFERENCIA', 1003, 'Papelaria Alpha', 1, 'VENCIDO', CURRENT_TIMESTAMP, 190.75),
(1004, CURRENT_TIMESTAMP, null, '2026-09-01', 'Pedido de pen drives personalizados', 'BOLETO', 1001, 'FotoPrint Insumos', 2, 'PENDENTE', CURRENT_TIMESTAMP, 359.40),
(1005, CURRENT_TIMESTAMP, null, '2026-08-25', 'Conta cancelada de fornecedor inativo', 'BOLETO', 1004, 'Fornecedor Inativo', 1, 'CANCELADO', CURRENT_TIMESTAMP, 99.90);

MERGE INTO register (id, data_time, edited, observation, type, user_id)
KEY(id)
VALUES
(1001, TIMESTAMP '2026-08-10 08:00:00', false, 'Entrada massa local', 'ENTRADA', 1),
(1002, TIMESTAMP '2026-08-10 12:00:00', false, 'Saida almoco massa local', 'SAIDA', 1),
(1003, TIMESTAMP '2026-08-10 13:00:00', false, 'Retorno almoco massa local', 'ENTRADA', 1),
(1004, TIMESTAMP '2026-08-10 18:05:00', false, 'Saida massa local', 'SAIDA', 1),
(1005, TIMESTAMP '2026-08-11 08:12:00', true, 'Registro editado para testar auditoria visual', 'ENTRADA', 1),
(1006, TIMESTAMP '2026-08-11 12:03:00', false, 'Saida almoco massa local', 'SAIDA', 1),
(1007, TIMESTAMP '2026-08-11 13:05:00', false, 'Retorno almoco massa local', 'ENTRADA', 1),
(1008, TIMESTAMP '2026-08-11 18:00:00', false, 'Saida massa local', 'SAIDA', 1),
(1009, TIMESTAMP '2026-08-12 08:05:00', false, 'Entrada operador teste', 'ENTRADA', 1001),
(1010, TIMESTAMP '2026-08-12 12:00:00', false, 'Saida operador teste', 'SAIDA', 1001);

ALTER TABLE users ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE products ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE services ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE packages ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE clients ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE addresses ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE contacts ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE suppliers ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE supplier_addresses ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE supplier_contacts ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE order_items ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE contas_receber ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE contas_pagar ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE register ALTER COLUMN id RESTART WITH 2000;
