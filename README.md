# Aplicativo de Gerenciamento de Academia (Gym Management App)

Um aplicativo completo para gerenciamento de academia, incluindo usuários, exercícios, treinos, acompanhamento de progresso e muito mais.

## 📱 Funcionalidades Principais

### 1. Gerenciamento de Usuários
- Cadastro de novos usuários com informações pessoais
- Edição e atualização de perfis
- Ativação/desativação de contas
- Filtro de usuários ativos/inativos
- Visualização detalhada de perfis

### 2. Gerenciamento de Exercícios
- Cadastro de exercícios com descrição, grupo muscular e nível de dificuldade
- Categorização por grupo muscular (pernas, braços, peito, etc.)
- Diferentes níveis de dificuldade (iniciante, intermediário, avançado)
- Visualização detalhada de cada exercício
- Ativação/desativação de exercícios no sistema

### 3. Gerenciamento de Treinos
- Criação de treinos personalizados
- Associação de múltiplos exercícios a um treino
- Definição de séries, repetições e intervalos
- Categorização por objetivo (hipertrofia, resistência, etc.)
- Níveis de dificuldade adaptados a cada perfil

### 4. Associação Usuário-Treino
- Atribuição de treinos específicos para cada usuário
- Acompanhamento de progresso individual
- Histórico de treinos realizados
- Adaptação de treinos conforme evolução do usuário

### 5. Perfil Físico
- Registro de medidas corporais (peso, altura, % de gordura)
- Histórico de evolução física
- Cálculos automáticos (IMC, etc.)
- Acompanhamento visual do progresso

### 6. Dashboard de Estatísticas
- Visão geral das informações do sistema
- Contagem de usuários, exercícios e treinos
- Análise de dados em tempo real
- Indicadores de desempenho

## 🗄️ Estrutura do Banco de Dados

O aplicativo utiliza um banco de dados Room com as seguintes tabelas:

1. **Usuários (usuarios)**: Armazena informações dos usuários da academia
2. **Perfil Físico (perfis_fisicos)**: Registra medidas e evolução física dos usuários
3. **Exercícios (exercicios)**: Cataloga todos os exercícios disponíveis
4. **Treinos (treinos)**: Contém os treinos personalizados
5. **Usuário-Treino (usuario_treino)**: Tabela de relacionamento entre usuários e treinos
6. **Treino-Exercício (treino_exercicio)**: Tabela de relacionamento entre treinos e exercícios

## 🔧 Tecnologias Utilizadas

- **Arquitetura MVVM**: Padrão moderno de desenvolvimento Android
- **Room Database**: Para persistência de dados local
- **Navigation Component**: Para navegação entre telas
- **LiveData & ViewModel**: Para separação de responsabilidades e observação de dados
- **ExecutorService**: Para operações assíncronas
- **Material Design**: Para interface visual moderna

## 🎨 Design Visual

- **Esquema de cores**: Vermelho (#E53935, #B71C1C) e Preto (#212121)
- **Tipografia**: Fonte sans-serif do sistema Android
- **Componentes**: Material Design para cards, botões e elementos de interface

## 📈 Futuras Melhorias

- Implementação de gráficos de progresso
- Sistema de notificações para lembretes de treino
- Exportação de treinos em PDF
- Sincronização com aplicativos de saúde
- Recursos de gamificação para incentivar a prática regular

## 🧰 Requisitos Técnicos

- Android 5.0 (API 21) ou superior
- 50 MB de espaço disponível
- Permissão para armazenamento (opcional, para backup)

## 👨‍💻 Desenvolvimento e Manutenção

Este aplicativo foi desenvolvido utilizando o Android Studio e segue as melhores práticas de desenvolvimento Android.

- **Tratamento de Erros**: Sistema robusto de log e captura de exceções
- **Testes**: Implementação de testes unitários para principais funcionalidades
- **Otimização**: Uso eficiente de recursos para garantir desempenho em diferentes dispositivos
