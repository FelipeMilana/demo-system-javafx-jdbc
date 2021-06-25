# System JavaFx JDBC
[![NPM](https://img.shields.io/npm/l/java)](https://github.com/FelipeMilana/demo-system-javafx-jdbc/blob/master/LICENSE)

# Sobre o projeto

Este projeto consiste em uma aplicação desktop de um sistema de formulário em Java, que utiliza o padrão DAO (Data Access Object) para fazer a integração com o banco de dados relacional. Além disso, 
utilizamos o JavaFx para criar a interface gráfica do sistema.

O banco de dados utilizado foi o MySQL.

## Layout 

<p>
  <img width="500" height="357" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc%231.png>
  <img width="500" height="357" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc%232.png>
</p>

<p>
  <img width="500" height="270" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc%233.png>
  <img width="445" height="213" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc%234.png>
</p>

<p>
  <img width="368" height="365" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc%235.png>
  <img width="500" height="357" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc%236.png>
</p>

## Modelo conceitual
<p>
  <img width="619" height="219" src=https://github.com/FelipeMilana/Assets/blob/main/demo-javafx-jdbc.png>
</p>

## Integrações realizadas
- Biblioteca MySQLConnector
- Biblioteca JavaFX

## Como executar o projeto

Pré-requisitos: 
- Java 11
- JavaFX 11
- Variáveis de ambiente: JAVA_HOME e PATH_TO_FX

```bash
# Clonar repositório
git clone: https://github.com/FelipeMilana/demo-system-javafx-jdbc.git

# Abrir projeto na IDE Eclipse

# Alterar db.properties 
Utilize dados próprios 

# Exportar arquivo .jar
Export -> java/runnable JAR file -> next
- Selecionar main class
- Selecionar pasta de destino (Ex: myApp)
- Library handling: 3rd option

# Pasta myApp
Adicionar o db.properties

# Executar o seguinte comando em cmd/myApp
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp demo-system-javafx-jdbc.jar application.Main
```

## Como criar app no desktop

```bash
# Criar um arquivo.bat na pasta myApp

# Adicionar o seguinte comando
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp demo-system-javafx-jdbc.jar application.Main

# Criar um atalho

# Selecionar o arquivo.bat
```

# Autor

Felipe Milana

[![NPM](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/felipemilana) 
