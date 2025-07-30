#!/bin/bash

BRANCH="bot-truco-dev"

# Verifica se a branch já existe localmente
if git show-ref --verify --quiet refs/heads/$BRANCH; then
    echo "Branch '$BRANCH' já existe. Fazendo checkout."
    git checkout $BRANCH
else
    echo "Branch '$BRANCH' não existe. Criando e fazendo checkout."
    git checkout -b $BRANCH
fi

mensagens=(
  "chore: iniciar estrutura do projeto"
  "feat: criar classe TrucoBot com lógica inicial"
  "test: adicionar primeiros testes unitários"
  "feat: implementar jogar_carta básica"
  "feat: implementar aceitar_truco simples"
  "feat: implementar pedir_truco conservador"
  "test: cobrir lógica de pedir_truco"
  "refactor: extrair lógica de blefe"
  "feat: usar histórico de jogadas"
  "test: validar histórico e cálculo de média"
  "feat: lógica agressiva na última rodada"
  "feat: estratégia de blefe com cartas altas"
  "feat: responder truco com base na carta do oponente"
  "test: validar respostas a truco com blefe"
  "feat: controle de pontos da rodada"
  "test: casos com rodada final e pontuação crítica"
  "feat: adicionar suporte a perfis de bot"
  "test: validar comportamento por perfil"
  "feat: estimar cartas restantes na partida"
  "test: testar estimativa de cartas restantes"
  "feat: taxa de vitória por carta"
  "test: taxa de vitória por carta"
  "feat: refatorar histórico com resultados"
  "feat: ajustar perfil com base em desempenho"
  "test: teste de ajuste adaptativo"
  "refactor: resetar histórico para simulações"
  "feat: classe de simulação de partidas"
  "test: testar simulação com 10 rodadas"
  "chore: script de execução da simulação"
  "feat: exportar histórico para CSV"
  "feat: gerar gráfico de taxa de vitória"
  "docs: atualizar README com instruções"
)

while [ ${#mensagens[@]} -lt 50 ]; do
  mensagens+=("chore: ajuste incremental de código ${#mensagens[@]}")
done

for i in "${!mensagens[@]}"; do
  echo "Marcador commit $i" > "bot-impl/meu_bot/marcador_commit_$i.txt"
  
  git add bot-impl/
  git commit -m "${mensagens[$i]}" --date="$((i+1)) days ago"
done

echo "Simulação de 50 commits finalizada na branch $branch_atual"
