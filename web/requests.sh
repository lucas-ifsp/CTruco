#
#  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
#  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
#
#  This file is part of CTruco (Truco game for didactic purpose).
#
#  CTruco is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  CTruco is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
#

#/!bin/bash

USER_PAYLOAD="{\"username\" : \"lucas\",\"email\" : \"lucas@email.com\"}"
echo "POSTING $USER_PAYLOAD"
UUID=$(curl -s -H "Content-Type:application/json" -d "$USER_PAYLOAD" http://localhost:8080/api/v1/user | jq '.uuid' | tr -d '"')
echo "RECEIVED: $UUID"
echo "---"

echo "GETTING from: http://localhost:8080/api/v1/user/by_uuid/${UUID}"
USER=$(curl -s GET http://localhost:8080/api/v1/user/by_uuid/"$UUID" | jq '.')
echo "RECEIVED: $USER"
echo "---"


GAME_PAYLOAD="{\"userUuid\" : \"$UUID\",\"botName\" : \"MineiroBot\"}"
echo "POSTING $GAME_PAYLOAD"
INTEL_1=$(curl -s -H "Content-Type:application/json" -d "$GAME_PAYLOAD" http://localhost:8080/api/v1/game/user_bot | jq '.')
echo "RECEIVED: $INTEL_1"
echo "---"


echo "GETTING from: http://localhost:8080/api/v1/game/player/${UUID}/in_turn"
IN_TURN=$(curl -s http://localhost:8080/api/v1/game/player/"$UUID"/in_turn | jq '.playerTurn')
echo "RECEIVED: $IN_TURN"
echo "---"


echo "GETTING from: http://localhost:8080/api/v1/game/player/${UUID}/cards"
FIRST_CARD=$(curl -s http://localhost:8080/api/v1/game/player/"$UUID"/cards | jq '.cards' | jq '.[0]')
echo "RECEIVED: $FIRST_CARD"
echo "---"


echo "POSTING http://localhost:8080/api/v1/game/player/${UUID}/card/play"
INTEL_2=$(curl -s -H "Content-Type:application/json" -d "$FIRST_CARD" http://localhost:8080/api/v1/game/player/"$UUID"/card/play | jq '.')
echo "RECEIVED: $INTEL_2"
echo "---"

echo "POSTING http://localhost:8080/api/v1/game/player/${UUID}/points/raise"
INTEL_3=$(curl -s -X POST http://localhost:8080/api/v1/game/player/"$UUID"/points/raise | jq '.')
echo "RECEIVED: $INTEL_3"
echo "---"

#INTEL_SINCE=$(curl -s GET http://localhost:8080/api/v1/game/player/"$UUID"/intel_since/{{lastIntelTimestamp}})
#echo "RECEIVED: $INTEL_SINCE"






