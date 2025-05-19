import random
from meu_bot.bot import TrucoBot

class SimuladorPartidas:
    def __init__(self, n_partidas=100):
        self.n_partidas = n_partidas
        self.resultados = {'vitorias_bot': 0, 'vitorias_oponente': 0}

    def simular(self):
        for _ in range(self.n_partidas):
            bot = TrucoBot()
            oponente = TrucoBot(perfil="cauteloso")

            vitorias_bot = 0
            vitorias_oponente = 0

            for rodada in range(3):
                mao_bot = random.sample(range(1, 14), 3)
                mao_oponente = random.sample(range(1, 14), 3)

                carta_bot = bot.jogar_carta(mao_bot, rodada + 1)
                carta_op = oponente.jogar_carta(mao_oponente, rodada + 1)

                if carta_bot > carta_op:
                    vitorias_bot += 1
                    bot.registrar_resultado(carta_bot, True)
                else:
                    vitorias_oponente += 1
                    bot.registrar_resultado(carta_bot, False)

            if vitorias_bot > vitorias_oponente:
                self.resultados['vitorias_bot'] += 1
            else:
                self.resultados['vitorias_oponente'] += 1

        return self.resultados
