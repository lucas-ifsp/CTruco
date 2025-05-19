import unittest
from meu_bot.bot import TrucoBot

class TestTrucoBot(unittest.TestCase):

    def setUp(self):
        self.bot = TrucoBot()

    def test_jogar_carta_primeira_rodada(self):
        self.assertEqual(self.bot.jogar_carta([3, 7, 12], 1), 12)
        self.assertEqual(self.bot.jogar_carta([3, 7, 10], 1), 3)

    def test_pedir_truco_com_historico_baixo(self):
        self.bot.analisar_jogada('4')
        self.bot.analisar_jogada('5')
        self.assertTrue(self.bot.pedir_truco(7, [2, 4, 6]))

    def test_aceitar_truco_agressivo(self):
        bot_agressivo = TrucoBot(perfil="agressivo")
        self.assertTrue(bot_agressivo.aceitar_truco(3, 0, [3, 5, 9]))

    def test_taxa_vitoria_por_carta(self):
        self.bot.registrar_resultado(7, True)
        self.bot.registrar_resultado(7, False)
        self.bot.registrar_resultado(10, True)
        taxas = self.bot.taxa_vitoria_por_carta()
        self.assertAlmostEqual(taxas[7], 0.5)
        self.assertAlmostEqual(taxas[10], 1.0)

    def test_ajuste_adaptativo_para_agressivo(self):
        for _ in range(8):
            self.bot.registrar_resultado(10, True)
        self.assertEqual(self.bot.perfil, "agressivo")

if __name__ == "__main__":
    unittest.main()
