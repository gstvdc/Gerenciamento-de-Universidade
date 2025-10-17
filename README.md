# 🎓 Sistema de Gerenciamento de Universidade

<h1 align="center">🎓 Sistema de Gerenciamento de Universidade</h1>


![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007ACC?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-Desenvolvido-green)

---

## 🧩 Sobre o Projeto

O **Sistema de Gerenciamento de Universidade** é uma aplicação **desktop em Java (Swing)** com integração ao **PostgreSQL**, criada para facilitar o **gerenciamento acadêmico** de uma instituição de ensino.  
O sistema permite o **cadastro e administração de cursos, disciplinas, fases e professores**, armazenando tudo de forma segura em banco de dados.

Seu código segue o **padrão DAO (Data Access Object)**, garantindo organização, separação de responsabilidades e facilidade de manutenção.

---

## 🚀 Funcionalidades Principais

✅ Cadastro de **Cursos**  
✅ Cadastro de **Disciplinas**  
✅ Cadastro de **Professores**  
✅ Cadastro de **Fases Acadêmicas**  
✅ Conexão com banco **PostgreSQL** via JDBC  
✅ Interface amigável com **Java Swing**

---

## ⚙️ Tecnologias Utilizadas

| Tecnologia | Descrição |
|-------------|------------|
| ☕ **Java SE 8+** | Linguagem base da aplicação |
| 🧰 **Swing** | Interface gráfica desktop |
| 🗃️ **PostgreSQL** | Banco de dados relacional |
| 🔌 **JDBC Driver** | Conexão entre Java e PostgreSQL |
| 🧱 **Padrão DAO** | Estrutura de acesso aos dados |
| 🧑‍💻 **Eclipse IDE** | Ambiente de desenvolvimento |

---

## 🗄️ Configuração do Banco de Dados

1️⃣ Crie o banco de dados no PostgreSQL:
```sql
CREATE DATABASE sistema_gerenciamento;
```

2️⃣ Configure a conexão no arquivo `src/banco/conexao.java`:
```java
String url = "jdbc:postgresql://localhost:5432/sistema_gerenciamento";
String user = "postgres";
String password = "SUA_SENHA_AQUI";
Connection conexao = DriverManager.getConnection(url, user, password);
```

3️⃣ Adicione o **driver JDBC do PostgreSQL** ao projeto:
- Clique com o botão direito no projeto → **Build Path → Configure Build Path → Add External JARs…**
- Selecione o arquivo `postgresql-<versão>.jar` e clique em **Apply and Close**.

---

## ▶️ Como Executar

### 🧠 No Eclipse
1. Importe o projeto (**File → Open Projects from File System...**)
2. Abra a classe `Main.java` em `src/telalogin/`
3. Clique com o botão direito → **Run As → Java Application**

### 📦 Gerar o .jar executável
1. **File → Export → Runnable JAR file**
2. Escolha `telalogin.Main` como classe principal
3. Marque *Package required libraries into generated JAR*
4. Salve como `SistemaGerenciamento.jar`
5. Execute:
```bash
java -jar SistemaGerenciamento.jar
```

---

## 🧱 Estrutura do Projeto

```
SistemaGerenciamento/
├── src/
│   ├── banco/          # DAOs e conexão com o banco
│   └── telalogin/      # Telas e lógica da interface Swing
├── resources/
│   └── icons/          # Ícones da interface
├── bin/                # Classes compiladas (não versionadas)
├── .classpath
├── .project
├── .gitignore
└── README.md
```

---

## 💡 Autor

**Gustavo Constante**  
www.linkedin.com/in/gstvdc
