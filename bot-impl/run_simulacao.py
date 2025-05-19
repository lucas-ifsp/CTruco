from meu_bot.simulador import SimuladorPartidas

if __name__ == "__main__":
    sim = SimuladorPartidas(n_partidas=100)
    resultado = sim.simular()
    print("Vitórias do Bot:", resultado['vitorias_bot'])
    print("Vitórias do Oponente:", resultado['vitorias_oponente'])
