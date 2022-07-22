# Wishlist Challenge

Wishlist Challenge é um serviço de API REST que disponibiliza alguns recursos para manutenção de uma lista de desejos.

O serviço permite a execução de rotinas na base de dados das wishlist. Ao todo são permitidos quatro operações, sendo elas:
    
1. Inserir um novo item em uma lista;
2. Remover um item existe em alguma lista;
3. Consultar todos os itens de alguma lista;
4. Verificar se determinado produto encontra-se na lista do usuário.

Um **usuário (cliente)** pode manter até **20 produtos** em sua **wishlist**.

# Recursos disponibilizados
- Listar todos os produtos contidos na wishlist do usuário (`GET /wishlist/client/{userId}`)

  Esse recurso permite que o usuário liste todos os itens que ele mantém na sua lista.

    - **Campos de entrada**:
        1. Codigo do Usuário (cliente) (_texto_) - **obrigatório**
        2. Código do Produto (_texto_) - **obrigatório**

    - **Validações realizadas:**
        1. Verifica se o usuário tem alguma lista cadastrada.


- Verificar a existência de um produto na lista de um usuário (`GET /wishlist/client/{userId}/product/{productId}`)

  Esse recurso permite que o usuário (cliente) verifique se determinado produto já se encontra em sua lista de desejos.

    - **Campos de entrada:**
        1. Código do Usuário (_texto_) - **obrigatório**
        2. Código do Produto (_texto_) - **obrigatório**


- Inserir um novo produto na lista do usuário (`POST /wishlist/client/{userId}/product/{productId}`)

  Esse recurso insere um novo item na lista de desejos do usuário (cliente).

    - **Campo de entrada:**
        1. Código do Usuário (_texto_) - **obrigatório**
        2. Código do Produto (_texto_) - **obrigatório**
    - **Validações realizadas:**
        1. Verifica se o produto já existe na lista do usuário;
        2. Verifica se a quantidade de itens contidas na lista é inferior ao limite definido (20 itens).


- Remove um item da lista de desejos do usuário (`DELETE /wishlist/client/{userId}/product/{productId}`)

  Esse recurso permite remover um item da lista de desejo do usuário (cliente).

    - **Campo de entrada:**
        1. Código do Usuário (_texto_) - **obrigatório**
        2. Código do Produto (_texto_) - **obrigatório**
    - **Validações realizadas:**
        1. Verifica se o produto informado existe na lista de desejo do usuário (cliente).



# Limitações
Não foram implementadas rotinas de segurança de acesso a API (autenticação e autorização), por exemplo, OAuth2 Security (https://oauth.net/2/). Desta forma toda
requisição recebida é processada, mediante as validações descritas no item anterior.

# Linguagem utilizada, framework e versão

Para realizar o desenvolvimento inicial do serviço, foi utilizado Java 18 com SpringBoot (https://spring.io/projects/spring-boot) em sua versão 2.7.1.

# Recursos utilizados:

A seguir são listados os principais recursos adicionados utilizados no serviço:
2. Lombok (https://projectlombok.org/) - biblioteca Java que cria automaticamente alguns recursos para manipular objetos;
3. MongoDB (https://www.postgresql.org/) - sistema de gerenciamento de banco de dados relacional que persiste os dados processados pelo serviço;
4. Hibernate (https://hibernate.org/) - framework para mapeamento objeto-relacional responsável por mapear os modelos de dados do serviço com as tabelas do PostgreSQL;
5. JUnit 5 (https://junit.org/junit5/) - framework utilizado para realizar testes unitários e integrados. 
6. Mockito (https://site.mockito.org/) - estrutura de teste que permite a criação de objetos duplos de teste em testes de unidade automatizados;
7. JoCoCo (https://www.eclemma.org/jacoco/) - biblioteca de cobertura de códigos;
8. Swagger (https://swagger.io/) - framework composto por diversas ferramentas que auxilia a descrição, consumo e visualização de serviços de uma API REST.


# Testes

Foram realizados dois tipos de testes automatizados: unitário e integração.

- **Unitário e Integração**

  Foram desenvolvidos 56 testes, sendo eles 16 testes unitários e 9 testes de integração.
  Ao todo, foram cobertos 116 linhas de código, o que corresponde a 95% das linhas de código do serviço.


# Registro de Log

Todos os erros que acontecem no serviço são registrados (_Logados_). O serviço captura esses erros, trata e retorna ao
usuário uma mensagem amigável, além de registrar em arquivo no servidor essa massagem.

Os principais erros do sistema retornam código de status de resposta HTTP 400 (_Bad Request_), porém pode existir algumas
situações não mapeadas que o servidor retorne código de status de resposta HTTP 500 (_Internal Server Error_).

# Documentação do código

O serviço utiliza a ferramenta Swagger (https://swagger.io/) para descrever as APIs disponíveis (`/swagger-ui/`).

# Build

O serviço foi desenvolvido utilizando o Gradle 7.4.2 (https://gradle.org/).

Para construir o projeto o usuário deverá possuir o Gradle instalado no seu computador.

No terminal do sistema operacional, vá até à pasta raiz do projeto e execute o comando:

`gradle wrapper`

Após finalizar a execução anterior execute:

- Windows: `.\gradlew.bat clean build`
- Linux: `.\gradlew clean build`


# Run

Foi disponibilizado um arquivo _Dockerfile_ para criação da imagem do serviço. Além desse
arquivo também foi disponibilizado um arquivo _docker-compose.yml_ com as configurações necessárias
para iniciar o serviço localmente.

O arquivo _docker-compose.yml_ deve ser alterado para informar as credenciais de acessos do banco de dados
(MongoDB).

Antes de executar o comando Docker é necessário criar (build) o projeto.
No momento da execução do _docker-compose.yml_ o _Dockerfile_ também será executado, para tanto é necessário
possuir o Docker-compose (https://docs.docker.com/compose/) instalado e configurado em sua máquina.

# Autor e Contato

- Autor: Vanderson Sampaio
- Email: vandersons.sampaio@gmail.com
- Github: https://github.com/vandersonsampaio
- Linkedin: https://www.linkedin.com/in/vanderson-sampaio-399973158/