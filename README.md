# Trens Paralelos
**Trabalho de programação concorrente UESB, algoritmos de exclusão mútua.**


## Introdução
Este programa tem como objetivo simular um cenário em que dois processos (trens) operam simultaneamente,  
compartilhando duas regiões críticas (trilhos únicos) de forma que quase não exista condições de corrida.  
A exclusão mútua é assegurada por três algoritmos: Variável de Travamento, Alternância Estrita e Solução de Peterson.


## Demonstração
![Demonstração](https://github.com/user-attachments/assets/87ad74a1-59bc-4fab-9631-06f548d29a14)


## Principais Funcionalidades
**Alterar velocidade dos trens:** A mudança de velocidade permite ao usuário testar todos os cenários possíveis.  

**Alterar ponto de partida dos trens:** A mudança do ponto de partida permite que o teste seja feito tanto em sentidos iguais quanto em sentidos opostos.  

**Alterar algoritmo de exclusão mútua:** A mudança do algoritmo permite testar as vantagens e desvantagens de cada um.


## Requisitos
Versão do java: jdk8 ou superior.  
Caso a versão do java seja maior que 9, é necessária instalação do javafx.


## Execução
Compile o arquivo Principal.java, e depois execute o programa a partir do Principal.class.


## Clone
git clone https://github.com/Guiow/Trens_Paralelos_PC.git
