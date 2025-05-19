/*
 * Este arquivo é parte do projeto CTruco.
 *
 * Copyright (C) 2024 PaolaTmpr
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo
 * sob os termos da Licença Pública Geral GNU conforme publicada pela Free Software Foundation,
 * na versão 3 da Licença, ou (a seu critério) qualquer versão posterior.
 *
 * Este programa é distribuído na esperança de que seja útil, mas SEM NENHUMA GARANTIA;
 * sem mesmo a garantia implícita de COMERCIALIZAÇÃO ou ADEQUAÇÃO A UM DETERMINADO FIM.
 * Veja a Licença Pública Geral GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa.
 * Se não, veja <https://www.gnu.org/licenses/>.
 */

package com.paola.pedro;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RoundClosingCapucina {

    public enum VencedorRodada {
        BOT, ADVERSARIO, EMPATE
    }

    private final Map<Integer, VencedorRodada> resultados = new HashMap<>();

    // Registra o vencedor de uma rodada (1 a 3)
    public void registrarResultadoRodada(int numeroRodada, VencedorRodada vencedor) {
        if (numeroRodada < 1 || numeroRodada > 3) {
            throw new IllegalArgumentException("Número da rodada deve ser 1, 2 ou 3.");
        }
        resultados.put(numeroRodada, vencedor);
    }

    // Retorna o vencedor geral da rodada após até 3 partidas
    public VencedorRodada determinarVencedorRodada() {
        EnumMap<VencedorRodada, Integer> contagem = new EnumMap<>(VencedorRodada.class);
        contagem.put(VencedorRodada.BOT, 0);
        contagem.put(VencedorRodada.ADVERSARIO, 0);

        for (int i = 1; i <= 3; i++) {
            VencedorRodada resultado = resultados.get(i);
            if (resultado == null) continue;

            if (resultado == VencedorRodada.BOT || resultado == VencedorRodada.ADVERSARIO) {
                contagem.put(resultado, contagem.get(resultado) + 1);
            }

            // Encerramento antecipado se alguém vence duas
            if (contagem.get(VencedorRodada.BOT) == 2) return VencedorRodada.BOT;
            if (contagem.get(VencedorRodada.ADVERSARIO) == 2) return VencedorRodada.ADVERSARIO;
        }

        int bot = contagem.get(VencedorRodada.BOT);
        int adv = contagem.get(VencedorRodada.ADVERSARIO);

        if (bot == adv) return VencedorRodada.EMPATE;
        return (bot > adv) ? VencedorRodada.BOT : VencedorRodada.ADVERSARIO;
    }

    // Limpa os resultados para uma nova rodada
    public void resetarRodada() {
        resultados.clear();
    }
}

