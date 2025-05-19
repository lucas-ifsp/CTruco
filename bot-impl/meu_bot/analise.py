import matplotlib.pyplot as plt

def plotar_taxa_vitoria_por_carta(bot):
    taxas = bot.taxa_vitoria_por_carta()
    cartas = list(taxas.keys())
    vitorias = list(taxas.values())

    plt.figure(figsize=(10, 5))
    plt.bar(cartas, vitorias, color='green')
    plt.xlabel("Carta")
    plt.ylabel("Taxa de Vitória")
    plt.title("Taxa de Vitória por Carta")
    plt.ylim(0, 1)
    plt.grid(axis='y')
    plt.show()
