package com.games.peter.lab2_matching_game;


import android.widget.ImageButton;

public class imbutton {
   private ImageButton imbtn;
   private int image;
   private boolean clicked;
   imbutton(ImageButton imbtn,int image,boolean clicked){
       this.imbtn= imbtn;
       this.image = image;
       this.clicked = clicked;
   }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public ImageButton getImbtn() {
        return imbtn;
    }

    public void setImbtn(ImageButton imbtn) {
        this.imbtn = imbtn;
    }
}
