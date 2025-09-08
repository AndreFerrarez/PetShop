# PetShop API

Aplicação RESTful para gerenciamento de Funcionários, Produtos, Fornecedores, Clientes e Pets.  
Desenvolvida com **Spring Boot**, **JPA**, **H2** e **JUnit**.

--

## Exemplo de JSON para testes 

POST http://localhost:8080/employees
Body: 
{
  "name": "John Doe",
  "role": "Veterinarian",
  "email": "john@example.com"
}

mvn test


## 🚀 Como executar

1. Clone o repositório  
2. Execute a aplicação:  
   ```bash
   mvn spring-boot:run
   
