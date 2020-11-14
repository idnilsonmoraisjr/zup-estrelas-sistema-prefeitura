#            Sistema de gestão de uma prefeitura

## Sobre 💡

O sistema consiste em um CRUD de gestão de uma prefeitura, onde seja possível gerenciar secretarias municipais, seus funcionários e projetos. 

## Tecnologias utilizadas ⚙

- [Java] Linguagem de programação (https://java.com/pt-BR/) 

- [Eclipse] IDE (https://www.eclipse.org/downloads/)

 - [MySQL] Banco de dados (https://www.mysql.com/)

 - [PostMan] Ferramenta de teste de requisições (https://www.postman.com/)


## Como começar

1 - Crie um banco de dados no MySQL usando esse comando
 "create database sistema_prefeitura;"

2 -  Clone na sua máquina o seguinte repositório "https://github.com/idnilsonmoraisjr/zup-estrelas-sistema-prefeitura.git"

3 - Execute na IDE de sua prefência

## Funcionamento
Com a aplicação em execução na Ide e o Postman com verbos HTTP para teste podemos testar as requisições.

Ex:


Cadastro de secretaria (Método post "http://localhost:8080/secretarias")

Tipos disponíveis atualmente:
ADMINISTRACAO, FAZENDA, COMUNICACAO, EDUCACAO,
SAUDE, TRANSPORTE,  LAZER, MEIO_AMBIENTE;

{<br>
    "area": "SAUDE",<br>
    "orcamentoProjetos": 20000,<br>
    "orcamentoFolha": 10000,<br>
    "telefone": "40028924",<br>
    "endereco": "Rua da saude, 123",<br>
    "site": "www.secsa.com.br",<br>
    "email": "secsa@email.com"<br>
}


<br>
Cadastro de projeto (Método post "http://localhost:8080/projetos")



{<br>
    "nome": "Saúde para todos",<br>
	"descricao": "Campanha de vacinação e realização de exames",<br>
	"custo": 50000,<br>
	"idSecretaria": 1<br>
}

<br>
Cadastro de funcionário (Método post "http://localhost:8080/funcionarios")
{<br>
    "nome": "Francisco",<br>
	"cpf": "12345678972",<br>
	"salario": 1100,<br>
    "idSecretaria": 1,<br>
	"funcao": "Auxiliar administrativo",<br>
	"concursado": true<br>
}


- Além disso existem métodos de listagem, remoção e consulta e alteração.
- Cada método possui suas regras de negócio específicas
- Abaixo estão mais algumas regras de negócio presentes no projeto:
    

##  Cadastro de Secretarias

O sistema deverá ser capaz de cadastrar e manter um cadastro sobre as secretarias da prefeitura, ex: Saúde, Educação, Trânsito.

Nós guardaremos os seguintes dados de uma secretaria:

Long idSecretaria,
String (que tal um Enum?) area (ex: saúde, trânsito),
Double orcamentoProjetos,
Double orcamentoFolha,
String telefone,
String endereco,
String site,
String email,
List<Funcionario> funcionarios,
List<Projeto> projetos;

Ao criar uma secretaria, só devem ser recebidos os campos area, orcamentoProjetos, orcamentoFolha, telefone, endereco, site e email (id sequencial auto gerado). Pra isso você deverá utilizar um DTO. Além disso, o sistema deverá validar se já existe uma secretaria para a área enviada e caso exista deverá um ocorrer um erro na criação da mesma. Crie endpoints para todas as operações de CRUD (Criação, Consulta, Alteração, Remoção e Listagem) de secretaria.

## Cadastro de Funcionarios

O sistema deverá ser capaz de cadastrar e manter um cadastro sobre os funcionários da prefeitura.

Nós guardaremos os seguintes dados de um funcionário:

Long idFuncionario,
String nome,
String cpf,
Double salario,
Long idSecretaria (referência à secretaria em que ele trabalha),
String funcao,
Boolean concursado,
LocalDate dataAdmissao.

Crie endpoints para todas as operações de CRUD (Criação, Consulta, Alteração, Remoção e Listagem) de funcionario.
Ao cadastrar um funcionário todos os campos deverão ser informados, menos o idFuncionario que será um id sequencial de auto-incremento. Ao cadastrar um Funcionário deverá ser validado se a secretaria na qual este será lotado há uma margem de orçamento de folha para conseguir arcar com seu salário, e se positivo este dado da secretaria deverá ser alterado para refletir a contratação deste funcionário.
Ao alterar um funcionário, caso haja uma alteração de secretaria deverá ser realizada a mesma verificação do cadastro inicial (verificação do orçamento), além disso deverá haver uma alteração nos orçamentos das duas secretarias para refletir a alocação de verba em uma e desalocação de verba em outra. Caso o salário do funcionário seja alterado, deverá ser aplicada a mesma validação e operações do cadastro, e também, deveremos nos lembrar de que um salário, de acordo com a CLT, não pode sofrer redução.
Ao remover um funcionário deveremos refletir no orçamento da secretaria na qual ele estava lotado a alteração em seu orçamento.

## Cadastro de Projetos

O sistema deverá ser capaz de cadastrar e manter um cadastro sobre os projetos da prefeitura.

Nós guardaremos os seguintes dados de um funcionário:

Long idProjeto,
String nome,
String descricao,
Double custo,
Long idSecretaria (referência à secretaria),
LocalDate dataInicio,
LocalDate dataEntrega,
Boolean concluido.

Crie endpoints para as seguintes operações de CRUD (Criação, Consulta, Alteração e Listagem) de projeto.
Ao cadastrar um projeto deveremos receber seu nome, descrição e custo. A data de início deverá ser a data de envio da requisição e por padrão, a data de entrega deverá ser nula, e o campo concluido deverá ser marcado como falso. Além disso, ao cadastrar um projeto o orçamento (e a existência) da secretaria deverá ser validado (deve existir orçamento disponível para o custo do projeto), e se positivo este dado da secretaria deverá ser alterado para refletir os custo deste projeto.
Na alteração de um projeto somente sua descrição poderá ser alterada.
Por fim, deveremos criar um endpoint para a conclusão de um projeto, quando este for chamado deveremos receber somente a data de entrega no corpo, uma validação importante é a de que a data de entrega deve ser maior do que a data de início. Ao processar a requisição o sistema deverá alterar o campo concluído pra true.
