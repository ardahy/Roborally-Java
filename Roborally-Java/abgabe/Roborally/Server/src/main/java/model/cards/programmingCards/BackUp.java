package model.cards.programmingCards;

import model.cards.Card;
import model.Player;
import model.field.Antenna;
import model.field.Wall;
import server.Server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is called when playing BackUp
 * @author Ilinur, Arda
 */

public class BackUp extends Card {

    String cardName = "BackUp";
    public String oldestPosition;

    @Override
    public String getCardName() {
        return cardName;
    }

    @Override
    public void playCard(Player player, Server server) {

        String direction = player.getRobotDirection();
        int xPosition = player.getRobotX();
        int yPosition = player.getRobotY();

        HashMap<String, Wall> wall = server.getWallMap();

        String oldPosition = xPosition + "_" + yPosition;
        String currentPosition;
        ArrayList<String> orientation;

        Wall wallField;


        switch (direction) {

            case "south":

                currentPosition = xPosition + "_" + (yPosition + 1);

                if (wall.containsKey(currentPosition) && wall.containsKey(oldPosition)) {
                    if (wall.containsKey(oldPosition)) {
                        wallField = wall.get(oldPosition);
                        orientation = wallField.getOrientation();
                        if (orientation.get(0).equals("bottom")) {
                            yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                            player.setRobotY(yPosition);
                        }
                    } else {
                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("top")) {
                                yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                            }
                        }
                    }
                } else {

                    if (wall.containsKey(currentPosition) | wall.containsKey(oldPosition)) {

                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();

                            if (orientation.get(0).equals("top")) {

                                yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                                player.setRobotY(yPosition);

                            } else {
                                yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                                oldPosition = currentPosition;
                            }
                        } else if (wall.containsKey(oldPosition)) {
                            wallField = wall.get(oldPosition);
                            orientation = wallField.getOrientation();

                            if (orientation.get(0).equals("bottom")) {

                                yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                                player.setRobotY(yPosition);

                            } else {
                                yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                                oldPosition = currentPosition;
                            }
                        }
                    } else {
                        yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                        player.setRobotY(yPosition);
                        oldestPosition = oldPosition;
                        oldPosition = currentPosition;
                    }

                    server.checkIfPit(player);
                }
                break;

            case "north":

                currentPosition = xPosition + "_" + (yPosition - 1);

                if (wall.containsKey(currentPosition) && wall.containsKey(oldPosition)) {
                    if (wall.containsKey(oldPosition)) {
                        wallField = wall.get(oldPosition);
                        orientation = wallField.getOrientation();
                        if (orientation.get(0).equals("top")) {
                            yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                            player.setRobotY(yPosition);
                        }

                        else {
                            yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                            player.setRobotY(yPosition);
                            oldPosition = currentPosition;
                        }
                    } else {
                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("bottom")) {
                                yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                            }

                            else {
                                yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                                oldPosition = currentPosition;
                            }
                        }
                    }
                } else {
                    if (wall.containsKey(currentPosition) | wall.containsKey(oldPosition)) {
                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();

                            if (orientation.get(0).equals("bottom")) {

                                yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                                player.setRobotY(yPosition);

                            } else {
                                yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                                oldPosition = currentPosition;
                            }
                        } else if (wall.containsKey(oldPosition)) {
                            wallField = wall.get(oldPosition);
                            orientation = wallField.getOrientation();

                            if (orientation.get(0).equals("top")) {

                                yPosition = Integer.parseInt(oldPosition.split("_")[1]);
                                player.setRobotY(yPosition);

                            } else {
                                yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                                player.setRobotY(yPosition);
                                oldPosition = currentPosition;

                            }
                        }
                    } else {
                        yPosition = Integer.parseInt(currentPosition.split("_")[1]);
                        player.setRobotY(yPosition);

                        oldestPosition = oldPosition;
                        oldPosition = currentPosition;

                    }

                    server.checkIfPit(player);
                }
                break;

            case "west":
                currentPosition = (xPosition - 1) + "_" + (yPosition);
                if (wall.containsKey(currentPosition) && wall.containsKey(oldPosition)) {
                    if (wall.containsKey(oldPosition)) {
                        wallField = wall.get(oldPosition);
                        orientation = wallField.getOrientation();
                        if (orientation.get(0).equals("right")) {
                            xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                            player.setRobotX(xPosition);
                        }
                        else {
                            xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                            player.setRobotX(xPosition);
                            oldPosition = currentPosition;

                        }
                    } else {
                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("left")) {
                                xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                            }
                            else {
                                xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                                oldPosition = currentPosition;

                            }
                        }
                    }
                } else {
                    if (wall.containsKey(currentPosition) | wall.containsKey(oldPosition)) {
                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("right")) {

                                xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                            } else {
                                xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                                oldPosition = currentPosition;

                            }
                        } else if (wall.containsKey(oldPosition)) {
                            wallField = wall.get(oldPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("left")) {

                                xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                                player.setRobotX(xPosition);

                            } else {
                                xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                                oldPosition = currentPosition;

                            }
                        }
                    } else {
                        xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                        player.setRobotX(xPosition);

                        oldestPosition = oldPosition;
                        oldPosition = currentPosition;
                    }
                }
                server.checkIfPit(player);
                break;

            case "east":
                currentPosition = (xPosition + 1) + "_" + (yPosition);

                if (wall.containsKey(currentPosition) && wall.containsKey(oldPosition)) {
                    if (wall.containsKey(oldPosition)) {
                        wallField = wall.get(oldPosition);
                        orientation = wallField.getOrientation();
                        if (orientation.get(0).equals("left")) {
                            xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                            player.setRobotX(xPosition);
                        }
                    } else {
                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("right")) {
                                xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                            }
                        }
                    }
                } else {
                    if (wall.containsKey(currentPosition) | wall.containsKey(oldPosition)) {

                        if (wall.containsKey(currentPosition)) {
                            wallField = wall.get(currentPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("left")) {
                                xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                                player.setRobotX(xPosition);

                            } else {
                                xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                                oldPosition = currentPosition;
                            }
                        } else if (wall.containsKey(oldPosition)) {
                            wallField = wall.get(oldPosition);
                            orientation = wallField.getOrientation();
                            if (orientation.get(0).equals("right")) {
                                xPosition = Integer.parseInt(oldPosition.split("_")[0]);
                                player.setRobotX(xPosition);

                            } else {
                                xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                                player.setRobotX(xPosition);
                                oldPosition = currentPosition;
                            }
                        }
                    } else {
                        xPosition = Integer.parseInt(currentPosition.split("_")[0]);
                        player.setRobotX(xPosition);
                        oldestPosition = oldPosition;
                        oldPosition = currentPosition;
                    }

                    server.checkIfPit(player);
                    break;
                }
        }
        pushRobots(player,server);
        player.addLastRegisterCard(cardName);
        checkAntenna(player, server);
    }
    public void checkAntenna (Player p, Server server) {
        HashMap<String, Antenna> antennaMap = server.getAntennaMap();
        int xPosition = p.getRobotX();
        int yPosition = p.getRobotY();
        String playerPosition = p.getRobotX() + "_" + p.getRobotY();
        if (antennaMap.containsKey(playerPosition)) {
            xPosition = Integer.parseInt(oldestPosition.split("_")[0]);
            yPosition = Integer.parseInt(oldestPosition.split("_")[1]);
            p.setRobotX(xPosition);
            p.setRobotY(yPosition);
        }
        boolean samePosition = false;
        for (Player pl: server.getPlayingPlayers()){
            if(p.getRobotX() == pl.getRobotX() && p.getRobotY() == pl.getRobotY() && !(p.equals(p))){
                samePosition = true;
                break;
            }
        }

        if(samePosition){
            pushRobots(p,server);
        }
    }
    public void pushRobots (Player player,Server server){
        boolean samePosition = false;
        for (Player p: server.getPlayingPlayers()){
            if(player.getRobotX() == p.getRobotX() && player.getRobotY() == p.getRobotY() && !(p.equals(player))){
                samePosition = true;
                break;
            }
        }

        if(!samePosition){
            return;
        }

        //Is the last robot in the push order next to a wall with the opposite orientation?
        int playerX = player.getRobotX();
        int playerY = player.getRobotY();
        int lastToPushX = 0;
        int lastToPushX2 = 0;
        int lastToPushX3 = 0;
        int lastToPushX4 = 0;
        int lastToPushY = 0;
        int lastToPushY2 = 0;
        int lastToPushY3 = 0;
        int lastToPushY4 = 0;


        switch (player.getRobotDirection()){
            case "south":
                lastToPushX = player.getRobotX();
                lastToPushY = player.getRobotY();
                boolean elementFound = true;
                boolean wallFound = false;
                while(elementFound && !wallFound){
                    elementFound = false;
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == lastToPushX && p.getRobotY() == lastToPushY + 1 ){
                            if(server.getWallMap().containsKey(lastToPushX + "_" + lastToPushY)){
                                if(server.getWallMap().get(lastToPushX + "_" + lastToPushY).getOrientation().get(0).equals("bottom")){
                                    wallFound = true;
                                }
                            }

                            int kw = lastToPushY+1;
                            if(server.getWallMap().containsKey(lastToPushX + "_" + kw)){
                                if(server.getWallMap().get(lastToPushX + "_" + kw).getOrientation().get(0).equals("top")) {
                                    wallFound = true;
                                }
                            }

                            if(!wallFound) {
                                lastToPushY++;
                                elementFound = true;
                            }


                        }
                    }
                }

                if(server.getWallMap().containsKey(lastToPushX + "_" + lastToPushY)){
                    if(server.getWallMap().get(lastToPushX + "_" + lastToPushY).getOrientation().get(0).equals("bottom")){
                        player.setRobotY(playerY-1);
                        return;
                    }
                }
                int k = lastToPushY+1;
                if(server.getWallMap().containsKey(lastToPushX + "_" + k)){
                    if(server.getWallMap().get(lastToPushX + "_" + k).getOrientation().get(0).equals("top")) {
                        player.setRobotY(playerY - 1);
                        return;
                    }
                }

                break;
            case "north":
                lastToPushX2 = player.getRobotX();
                lastToPushY2 = player.getRobotY();
                boolean elementFound2 = true;
                boolean wallFound2 = false;
                while(elementFound2 && !wallFound2){
                    elementFound2 = false;
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == lastToPushX2 && p.getRobotY() == lastToPushY2 -1){

                            if(server.getWallMap().containsKey(lastToPushX2 + "_" + lastToPushY2)){
                                if(server.getWallMap().get(lastToPushX2 + "_" + lastToPushY2).getOrientation().get(0).equals("top")){
                                    wallFound2 = true;
                                }
                            }

                            int kw2 = lastToPushY2+1;
                            if(server.getWallMap().containsKey(lastToPushX2 + "_" + kw2)){
                                if(server.getWallMap().get(lastToPushX2 + "_" + kw2).getOrientation().get(0).equals("bottom")) {
                                    wallFound2 = true;
                                }
                            }

                            if(!wallFound2){
                                lastToPushY2 --;
                                elementFound2 = true;
                            }

                        }
                    }

                }

                if(server.getWallMap().containsKey(lastToPushX2 + "_" + lastToPushY2)){
                    if(server.getWallMap().get(lastToPushX2 + "_" + lastToPushY2).getOrientation().get(0).equals("top")){
                        player.setRobotY(playerY+1);
                        return;
                    }
                }
                int k2 = lastToPushY2+1;
                if(server.getWallMap().containsKey(lastToPushX2 + "_" + k2)){
                    if(server.getWallMap().get(lastToPushX2 + "_" + k2).getOrientation().get(0).equals("bottom")) {
                        player.setRobotY(playerY + 1);
                        return;
                    }
                }

                break;
            case "west":
                lastToPushX3 = player.getRobotX();
                lastToPushY3 = player.getRobotY();
                boolean elementFound3 = true;
                boolean wallFound3 = false;

                while(elementFound3 && !wallFound3){
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == lastToPushX3-1 && p.getRobotY() == lastToPushY3){
                            if(server.getWallMap().containsKey(lastToPushX3 + "_" + lastToPushY3)){
                                if(server.getWallMap().get(lastToPushX3 + "_" + lastToPushY3).getOrientation().get(0).equals("left")){
                                    wallFound3 = true;
                                }
                            }

                            int kw3 = lastToPushX3-1;
                            if(server.getWallMap().containsKey(kw3 + "_" + lastToPushY3)){
                                if(server.getWallMap().get(kw3 + "_" + lastToPushY3).getOrientation().get(0).equals("right")) {
                                    wallFound3 = true;
                                }
                            }

                            if(!wallFound3){
                                lastToPushX3 --;
                                elementFound3 = true;
                            }

                        }
                    }
                }

                if(server.getWallMap().containsKey(lastToPushX3 + "_" + lastToPushY3)){
                    if(server.getWallMap().get(lastToPushX3 + "_" + lastToPushY3).getOrientation().get(0).equals("left")){
                        player.setRobotX(playerX+1);
                        return;
                    }
                }

                int k3 = lastToPushX3-1;
                if(server.getWallMap().containsKey(k3 + "_" + lastToPushY3)){
                    if(server.getWallMap().get(k3 + "_" + lastToPushY3).getOrientation().get(0).equals("right")) {
                        player.setRobotX(playerX + 1);
                        return;
                    }
                }
                break;
            case "east":
                lastToPushX4 = player.getRobotX();
                lastToPushY4 = player.getRobotY();
                boolean elementFound4 = true;
                boolean wallFound4 = false;

                while(elementFound4 && !wallFound4){
                    elementFound4 = false;
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == lastToPushX4+1 && p.getRobotY() == lastToPushY4 ){
                            if(server.getWallMap().containsKey(lastToPushX4 + "_" + lastToPushY4)){
                                if(server.getWallMap().get(lastToPushX4 + "_" + lastToPushY4).getOrientation().get(0).equals("right")){
                                    wallFound4 = true ;
                                }
                            }

                            int kw4 = lastToPushX4+1;
                            if(server.getWallMap().containsKey(kw4 + "_" + lastToPushY4)){
                                if(server.getWallMap().get(kw4 + "_" + lastToPushY4).getOrientation().get(0).equals("left")) {
                                    wallFound4 = true;
                                }
                            }

                            if(!wallFound4){
                                lastToPushX4 ++;

                                elementFound4 =true;
                            }
                        }
                    }
                }


                if(server.getWallMap().containsKey(lastToPushX4 + "_" + lastToPushY4)){
                    if(server.getWallMap().get(lastToPushX4 + "_" + lastToPushY4).getOrientation().get(0).equals("right")){
                        player.setRobotX(playerX-1);
                        return;
                    }
                }
                int k4 = lastToPushX4+1;
                if(server.getWallMap().containsKey(k4 + "_" + lastToPushY4)){
                    if(server.getWallMap().get(k4 + "_" + lastToPushY4).getOrientation().get(0).equals("left")) {
                        player.setRobotX(playerX - 1);
                        return;
                    }
                }

                break;


        }

        //Keep pushing until every robot in the order is pushed.
        switch (player.getRobotDirection()){
            case "south":
                for (int i = lastToPushY;i>= player.getRobotY();i--){
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == player.getRobotX() && p.getRobotY() == i && !(p.equals(player))){
                            int yToPush = p.getRobotY();
                            p.setRobotY(yToPush+1);
                        }
                    }
                }
                break;
            case "north":
                for (int i = lastToPushY2;i<= player.getRobotY();i++){
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == player.getRobotX() && p.getRobotY() == i && !(p.equals(player))){
                            int yToPush = p.getRobotY();
                            p.setRobotY(yToPush-1);
                        }
                    }
                }
                break;
            case "west":
                for (int i = lastToPushX3;i<= player.getRobotX();i++){
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == i && p.getRobotY() == player.getRobotY() && !(p.equals(player))){
                            int xToPush = p.getRobotX();
                            p.setRobotX(xToPush-1);
                        }
                    }
                }
                break;
            case "east":
                for (int i = lastToPushX4;i>= player.getRobotX();i--){
                    for(Player p: server.getPlayingPlayers()){
                        if(p.getRobotX() == i && p.getRobotY() == player.getRobotY() && !(p.equals(player))){
                            int xToPush = p.getRobotX();
                            p.setRobotX(xToPush+1);
                        }
                    }
                }
                break;
        }
        //player.addLastRegisterCard(cardName);

    }
}




