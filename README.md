# GYM Fitness - Aplicativo Android

## Visão Geral
GYM Fitness é um aplicativo Android para gerenciamento de treinos e exercícios físicos. Ele permite que usuários gerenciem seus perfis, exercícios e rotinas de treinamento de forma organizada e intuitiva.

## Funcionalidades
- **Gerenciamento de Usuários**: Cadastro e manutenção de perfis de usuários
- **Biblioteca de Exercícios**: Catálogo completo de exercícios organizados por categoria
- **Planos de Treino**: Criação e acompanhamento de rotinas de treinamento personalizadas
- **Interface Amigável**: Navegação intuitiva com menu lateral (drawer) para acesso rápido às funcionalidades

## Estrutura do Projeto
```
app/
├── src/
│   └── main/
│       ├── java/app/gym/
│       │   ├── data/       # Camada de dados e persistência
│       │   ├── ui/         # Interfaces de usuário e fragments
│       │   └── util/       # Utilitários e helpers
│       └── res/            # Recursos (layouts, strings, imagens, etc.)
```

## Tecnologias Utilizadas
- **Linguagem**: Java
- **SDK Mínimo**: Android SDK
- **Navegação**: Navigation Component com NavController
- **UI**: Material Design Components
- **Estrutura**: MVVM (Model-View-ViewModel) ou similar

## Requisitos
- Android Studio
- JDK 8 ou superior
- Dispositivo Android ou emulador

## Configuração do Projeto
1. Clone o repositório
2. Abra o projeto no Android Studio
3. Sincronize o projeto com os arquivos Gradle
4. Execute o aplicativo em um dispositivo ou emulador

## Funcionalidades Principais
- Tela inicial com resumo de atividades
- Gestão de perfis de usuários
- Cadastro e visualização de exercícios
- Criação e acompanhamento de treinos
- Menu de navegação lateral para acesso às funcionalidades

## Tratamento de Erros
O aplicativo inclui um sistema robusto de tratamento de erros com:
- Captura global de exceções não tratadas
- Logs detalhados para depuração
- Armazenamento de informações de crash para análise posterior

## Licença
O GYM Fitness é licenciado sob a Licença Apache 2.0. Consulte o arquivo LICENSE para mais informações.
---

Desenvolvido por Gabriel de Souza Valenço - 218124
