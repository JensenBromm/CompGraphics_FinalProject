package mainPackage;

import java.util.ArrayList;
import java.awt.Color;

public class GameScene {
    public int[][] map;

    public int mapWidth, mapHeight, width, height;
    public ArrayList<GameTexture> textures;

    public GameScene(int[][] m, int mapW, int mapH, ArrayList<GameTexture> tex, int w, int h) {
        map = m;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;
    }

    public int[] update (GameCamera camera, int[] pixels) {
        //First reset the pixels to just a floor and a roof
        //Create a floor
        for (int i = 0; i < pixels.length/2; i++) {
            if (pixels[i] != Color.DARK_GRAY.getRGB())
                pixels[i] = Color.DARK_GRAY.getRGB();
        }

        //Create a ceiling
        for (int i = pixels.length/2; i < pixels.length; i++) {
            if (pixels[i] != Color.GRAY.getRGB())
                pixels[i] = Color.GRAY.getRGB();
        }

        for (int x = 0; x < width; x = x + 1) {
            double cameraX = 2*x / (double)(width) - 1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;

            double sideDistX, sideDistY; //Length of ray from current position

            //Position on the map
            int mapX = (int)camera.xPos;
            int mapY = (int)camera.yPos;

            //Length of ray form current position to next side
            double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
            double wallDist;    //Wall distance from player that ray hits

            //Find the direction to go in x and y plane
            int stepX, stepY;
            boolean wall = false;
            int side = 0; //Did we hit the corner of a wall or face

            //Figure out which direction to step and how the initial distance to a side of the wall
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            }else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            }else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }

            //Check to see if ray has collided with a wall
            while (!wall) {
                //Go to next square
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }else{
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                //Check if that next square is a wall
                if(map[mapX][mapY] > 0)
                    wall = true;
            }

            //Get distance to the point of impact
            if (side == 0)
                wallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX);
            else
                wallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);

            //Now find the height of the wall based on how far away it is from the camera
            int lineHeight = height;
            if (wallDist > 0)
                lineHeight = Math.abs((int)(height / wallDist));
            else
                lineHeight = height;

            //Find the lowest and highest pixel to fill current vertical strip
            int drawStart = -lineHeight/2 + height/2;
            if (drawStart < 0)
                drawStart = 0;

            int drawEnd = lineHeight/2 + height/2;
            if (drawEnd >= height)
                drawEnd = height - 1;

            //Put textures onto that wall
            int texNum = map[mapX][mapY] - 1;
            double wallX;   //Exact position where wall was hit

            if (side == 1)
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX); //Y-axis wall
            else
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY); //X-axis wall

            wallX -= Math.floor(wallX);

            //x coordinate on the texture
            int texX = (int)(wallX * (textures.get(texNum).size));
            if ((side == 0 && rayDirX > 0) || (side == 1 && rayDirY < 0))
                texX = textures.get(texNum).size - texX - 1;

            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) /2;
                int color;

                if (side == 0)
                    color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).size)]& 8355700; //Numbers at the end change the colors to a tad darker
                else
                    color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).size)] >> 1) & 8355700;

                pixels[x + y*(width)] = color;
            }

        }

        return pixels;
    }
}
