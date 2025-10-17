🎓 Sistema de Gerenciamento Acadêmico (Java + PostgreSQL)

Aplicação desktop em Java (Swing) para gerenciar cursos, fases, disciplinas e professores.
Conecta ao PostgreSQL via JDBC e usa o padrão DAO para acesso aos dados.

✨ Funcionalidades

Cadastro de Cursos

Cadastro de Disciplinas

Cadastro de Professores

Cadastro de Fases

Importação de dados por arquivo

Interface Swing com ícones

🧰 Tecnologias

Java SE 8+

Swing

PostgreSQL (JDBC Driver)

Padrão DAO

📂 Estrutura do projeto
SistemaGerenciamento/
├─ src/
│  ├─ banco/          # DAOs e conexão (JDBC)
│  └─ telalogin/      # Telas Swing (ex.: Main)
├─ resources/icons/   # Ícones usados pela UI
├─ bin/               # Classes compiladas (gerado pelo Eclipse)
├─ .classpath
├─ .project
└─ README.md

🗄️ Banco de dados (PostgreSQL)

1️⃣ Crie o banco
CREATE DATABASE sistema_gerenciamento;

2️⃣ Configure a conexão em src/banco/conexao.java
String url = "jdbc:postgresql://localhost:5432/sistema_gerenciamento";
String user = "postgres";
String password = "SUA_SENHA_AQUI"; // evite subir senha real no GitHub
Connection conexao = DriverManager.getConnection(url, user, password);

3️⃣ Adicione o driver PostgreSQL no Eclipse

Clique com o botão direito no projeto → Build Path → Configure Build Path…

Aba Libraries → Add External JARs…

Selecione postgresql-<versão>.jar

Clique em Apply and Close

💡 Dica: baixe o driver JDBC em https://jdbc.postgresql.org/download

▶️ Como executar no Eclipse

Importe o projeto: File → Open Projects from File System…

Abra a classe principal: src/telalogin/Main.java

Clique com o botão direito → Run As → Java Application

📦 Gerar JAR executável (opcional)

File → Export → Runnable JAR file

Launch configuration: telalogin.Main

Export destination: SistemaGerenciamento.jar

Library handling: Package required libraries into generated JAR

Para executar:

java -jar SistemaGerenciamento.jar

🔒 Boas práticas de segurança

Nunca versione senhas reais.
Prefira variáveis de ambiente ou um arquivo config.properties fora do src/.

Use o .gitignore para evitar subir:

/bin/
.classpath
.project
/.settings/

📝 Licença

Este projeto pode ser distribuído sob a licença MIT:

MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction


💡 Projeto desenvolvido em Java + PostgreSQL como sistema acadêmico de estudo.
Sinta-se à vontade para contribuir ou sugerir melhorias!
