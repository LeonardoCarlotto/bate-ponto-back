-- Criar tabela de serviços
CREATE TABLE IF NOT EXISTS services (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Criar tabela de relacionamento pacote-serviços
CREATE TABLE IF NOT EXISTS package_services (
    package_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    PRIMARY KEY (package_id, service_id),
    FOREIGN KEY (package_id) REFERENCES packages(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_services_name ON services(name);
CREATE INDEX IF NOT EXISTS idx_services_active ON services(active);
CREATE INDEX IF NOT EXISTS idx_package_services_package_id ON package_services(package_id);
CREATE INDEX IF NOT EXISTS idx_package_services_service_id ON package_services(service_id);

-- Inserir alguns serviços de exemplo (opcional)
INSERT INTO services (name, description, price, active) VALUES 
('Manicure', 'Serviço de manicure completa', 50.00, TRUE),
('Pedicure', 'Serviço de pedicure completa', 60.00, TRUE),
('Massagem Relaxante', 'Massagem de 60 minutos', 120.00, TRUE),
('Corte de Cabelo', 'Corte e lavagem', 80.00, TRUE),
('Tratamento Facial', 'Limpeza e hidratação', 150.00, TRUE)
ON CONFLICT DO NOTHING;
