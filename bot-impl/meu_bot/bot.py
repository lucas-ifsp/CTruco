import random
from collections import defaultdict

class TrucoBot:
    def __init__(self, name="Bot Truco", perfil="balanceado"):
        self.name = name
        self.perfil = perfil
        self.historico_jogadas = []

    def analisar_jogada(self, carta_jogada):
        if isinstance(carta_jogada, dict):
            self.historico_jogadas.append(carta_jogada)
        else:
            self.historico_jogadas.append({'carta': int(carta_jogada), 'venceu': None})

    def calcular_media_jogadas(self):
        valores = [x['carta'] for x in self.historico_jogadas if x['carta'] is not None]
        return sum(valores) / len(valores) if valores else 0

    def registrar_resultado(self, carta_jogada, venceu):
        self.historico_jogadas.append({'carta': int(carta_jogada), 'venceu': venceu})
        self.ajustar_estilo_com_base_em_performance()

    def taxa_vitoria_por_carta(self):
        estatisticas = defaultdict(lambda: {'jogos': 0, 'vitorias': 0})
        for entrada in self.historico_jogadas:
            carta = entrada['carta']
            estatisticas[carta]['jogos'] += 1
            if entrada['venceu']:
                estatisticas[carta]['vitorias'] += 1
        return {carta: v['vitorias'] / v['jogos'] for carta, v in estatisticas.items() if v['jogos'] > 0}

    def ajustar_estilo_com_base_em_performance(self):
        taxas = self.taxa_vitoria_por_carta()
        if not taxas:
            return
        media_taxa = sum(taxas.values()) / len(taxas)
        if media_taxa > 0.7:
            self.perfil = "agressivo"
        elif media_taxa < 0.4:
            self.perfil = "cauteloso"
        else:
            self.perfil = "balanceado"

    def jogar_carta(self, mao, rodada, cartas_oponente=None, pontos_rodada=0):
        taxas = self.taxa_vitoria_por_carta()
        mao_ordenada = sorted(mao, key=lambda c: taxas.get(c, 0), reverse=True)

        if rodada == 1 and max(mao) >= 12:
            return mao_ordenada[0]
        if rodada == 3 and pontos_rodada < 6:
            return mao_ordenada[0]
        if cartas_oponente and max(cartas_oponente) >= 12:
            return mao_ordenada[len(mao)//2]
        return mao_ordenada[-1]

    def aceitar_truco(self, pontos_rodada, pontos_oponente, mao):
        if self.perfil == "agressivo":
            return True
        elif self.perfil == "cauteloso":
            return max(mao) >= 12 and pontos_rodada >= 8
        return max(mao) >= 11 or pontos_rodada >= 10

    def pedir_truco(self, pontos_rodada, mao):
        if self.perfil == "agressivo":
            return max(mao) >= 10
        elif self.perfil == "cauteloso":
            return max(mao) >= 12 and pontos_rodada >= 10
        return all(int(j['carta']) <= 7 for j in self.historico_jogadas) or max(mao) >= 12

    def estimar_cartas_restantes(self, cartas_jogadas):
        todas = set(range(1, 14))
        jogadas = set(cartas_jogadas + [j['carta'] for j in self.historico_jogadas])
        return list(todas - jogadas)

    def resetar_historico(self):
        self.historico_jogadas = []

    def exportar_historico_csv(self, caminho="historico_jogadas.csv"):
        import csv
        with open(caminho, mode='w', newline='') as arquivo:
            writer = csv.DictWriter(arquivo, fieldnames=["carta", "venceu"])
            writer.writeheader()
            for entrada in self.historico_jogadas:
                writer.writerow(entrada)
