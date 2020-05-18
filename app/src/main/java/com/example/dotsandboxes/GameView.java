package com.example.dotsandboxes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Random;
import java.util.Vector;


public class GameView extends View {
    Vibrator v;
    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint PaintU1,PaintU2,drawPaint,PaintU3,PaintU4,PaintCplayer;
    // stores next circle
    private Path pathU1 = new Path();
    private  Path pathU2 = new Path();
    private  Path pathU3 = new Path();
    private  Path pathU4 = new Path();
    private  Path path = new Path();
    private  Path TempPath = new Path();
    private  Path Temp1Path = new Path();
    private  Path TempCPath = new Path();

    Vector<Float> PosX = new Vector<>();
    Vector<Float> PosY = new Vector<>();
    Vector<Integer> pos = new Vector<>();


    int CurrUser = 1,PrevUser = 1;                                                     //1 - U1         2 - U2       3- U3          4-U4
    String CurrPlayer;
    int  Score1 = 0,Score2 = 0,Score3 = 0,Score4 = 0;
    boolean IsCompPlaying = false;
    int Difficulty = 0;
    float PrevX = -1,PrevY= -1,Prev2X,Prev2Y;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }






    /* A--,
       |  |
       '--D
     */

    public class square{
        public float Ax,Ay,Dx,Dy;
        public int nSidesCompleted = 0;
        public boolean LeftComp = false,RightComp = false, TopComp = false, BottomComp = false,isCompleted = false;

        public square(float a,float b,float c, float d){
            Ax = a;
            Ay = b;
            Dx = c;
            Dy = d;
        }

        public String toString()
        { return "square{" +
                    "LeftComp : " + LeftComp +'\n' +
                    "RightComp : " + RightComp +'\n' +
                    "TopComp : " + TopComp +'\n' +
                    "BottomComp : " + BottomComp +'\n' +
                    "isCompleted : " + isCompleted + '\n'+
                    "nsidescompleted : " + nSidesCompleted  ;
        }
    }

    ArrayList<square> Sqr = new ArrayList<square>();

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        PaintU1 = new Paint();
        PaintU1.setColor(Color.MAGENTA);
        PaintU1.setTextSize(80);
        PaintU1.setStyle(Paint.Style.FILL);

        PaintU2 = new Paint();
        PaintU2.setColor(Color.CYAN);
        PaintU2.setTextSize(80);
        PaintU2.setStyle(Paint.Style.FILL);

        PaintU3 = new Paint();
        PaintU3.setColor(Color.YELLOW);
        PaintU3.setTextSize(80);
        PaintU3.setStyle(Paint.Style.FILL);

        PaintU4 = new Paint();
        PaintU4.setColor(Color.GREEN);
        PaintU4.setTextSize(80);
        PaintU4.setStyle(Paint.Style.FILL);

        PaintCplayer = new Paint();
        PaintCplayer.setColor(Color.DKGRAY);
        PaintCplayer.setTextSize(50);
        PaintCplayer.setStyle(Paint.Style.FILL);
    }

    int width  = getMeasuredWidth();
    int height = getMeasuredHeight();
    int GridSide;
    int Radius =  Math.round(width/((3*GridSide - 2)*2)) ;
    int Gap = 6*Radius;
    int Cx = Radius;
    int Cy = Radius;
    int ArrCreate = 0;
    int nPlayers;
    Boolean IsGameOver = false;

    int x=0;
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable d = getResources().getDrawable(R.drawable.undo, null);
        d.setBounds(width - 100, 230, width, 330);
        d.draw(canvas);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        GridSide = (int) getTag() % 10;
        Log.i("TAG","GS: "+ GridSide);
        nPlayers = ((int) getTag() - GridSide) / 10;
        if (nPlayers > 4) {

            if (nPlayers == 5)
                Difficulty = 0;

            else if (nPlayers == 6)
                Difficulty = 1;

            nPlayers = 2;
            IsCompPlaying = true;
        }

        Log.i("TAG", " on creation Difficult = " + Difficulty);
        Radius = Math.round(width / ((3 * GridSide - 2) * 2));
        Gap = 6 * Radius;
        Cx = Radius;
        Cy = 400 + Radius;

        canvas.drawPath(pathU1, PaintU1);
        canvas.drawPath(pathU2, PaintU2);

        switch (CurrUser) {
            case 1:
                CurrPlayer = "Player 1";
                break;
            case 2:
                if (!IsCompPlaying)
                    CurrPlayer = "Player 2";
                else
                    CurrPlayer = "Computer";
                break;
            case 3:
                CurrPlayer = "Player 3";
                break;
            case 4:
                CurrPlayer = "Player 4";
                break;

        }

        if ((Score1 + Score2 + Score3 + Score4) != (GridSide - 1) * (GridSide - 1))
            canvas.drawText(CurrPlayer + "'s Turn", width / 2, Cy + width + 200, PaintCplayer);
        else {
            IsGameOver = true;
            CurrUser = -1;

            v.vibrate(VibrationEffect.createOneShot(1200, 250));
            canvas.drawText("GAME OVER ", width / 2 - 300, Cy + width + 200, PaintCplayer);
            if (!IsCompPlaying) {
                if (Score1 > Score2 && Score1 > Score3 && Score1 > Score4)
                    canvas.drawText("Player 1 WON!!!", width / 2 - 300, Cy + width + 300, PaintU1);
                else if (Score2 > Score1 && Score2 > Score3 && Score2 > Score4)
                    canvas.drawText("Player 2 WON!!!", width / 2 - 300, Cy + width + 300, PaintU2);
                else if (Score3 > Score1 && Score3 > Score2 && Score3 > Score1)
                    canvas.drawText("Player 3 WON!!!", width / 2 - 300, Cy + width + 300, PaintU3);
                else if (Score4 > Score1 && Score4 > Score2 && Score4 > Score3)
                    canvas.drawText("Player 4 WON!!!", width / 2 - 300, Cy + width + 300, PaintU4);
                else
                    canvas.drawText("DRAW ", width / 2 - 300, Cy + width + 300, PaintCplayer);
            } else {
                if (Score1 > Score2)
                    canvas.drawText("Player 1 WON!!!", width / 2 - 300, Cy + width + 300, PaintU1);
                else if (Score1 == Score2)
                    canvas.drawText("DRAW ", width / 2 - 300, Cy + width + 300, PaintCplayer);
                else
                    canvas.drawText("Computer WON!!! ", width / 2 - 300, Cy + width + 300, PaintU2);
            }
            IsCompPlaying = false;
        }

        canvas.drawText("Player 1 : " + Score1, 10, 100, PaintU1);
        if (!IsCompPlaying)
            canvas.drawText("Player 2 : " + Score2, width - 440, 100, PaintU2);
        else
            canvas.drawText("Comp : " + Score2, width - 440, 100, PaintU2);
        if (nPlayers > 2) {
            canvas.drawPath(pathU3, PaintU3);
            canvas.drawText("Player 3 : " + Score3, 10, height - 100, PaintU3);

            if (nPlayers > 3) {
                canvas.drawPath(pathU4, PaintU4);
                canvas.drawText("Player 4 : " + Score4, width - 440, height - 100, PaintU4);
            }
        }


        for(int i = 0; i < GridSide ; i++,Cy += Gap)
        {
            Cx = Radius;
            for(int j =0 ; j < GridSide ;j++,Cx += Gap)
            {
                canvas.drawCircle( Cx,Cy,Radius+10, drawPaint);
                if(ArrCreate == 0)
                    if(i!= GridSide-1 && j!= GridSide-1) {
                        Sqr.add(new square(Cx, Cy, Cx + Gap, Cy + Gap));
                        pos.add(x);
                        x++;
                    }
            }
        }


            ArrCreate = 1;
            //canvas.drawPath(path,PaintU4);

        }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!IsGameOver) {
                    if (!(IsCompPlaying && CurrUser == 2))
                        SideCheck(pointX, pointY);

                    if (!(IsCompPlaying))
                        Undo(pointX, pointY);

                    if (IsCompPlaying)
                        UndoComp(pointX, pointY);

                    if (IsCompPlaying && Difficulty == 1 && CurrUser == 2)
                        ComputerTurn();
                }
                  return true;

            case MotionEvent.ACTION_UP:
                if(!IsGameOver) {
                    if (!(IsCompPlaying && CurrUser == 2))
                        SideCheck(pointX, pointY);

                    if (!(IsCompPlaying))
                        Undo(pointX, pointY);

                    if (IsCompPlaying)
                        UndoComp(pointX, pointY);

                    if (IsCompPlaying && Difficulty == 1 && CurrUser == 2)
                        ComputerTurn();
                }

                break;
            default:
                return false;
        }
        postInvalidate();
        return true;
    }

    public void SideCheck(float X, float Y){
        path = new Path();
        int i = 0;
        int AlCompleted = 0;
        for(; i < Sqr.size() ; i++) {
            square temp = Sqr.get(i);

            if (CheckRect(temp.Ax - Radius, temp.Ax + Radius, temp.Ay, temp.Dy, X, Y) && (! temp.LeftComp)) {

                PosX.add(X);
                PosY.add(Y);
                if(CurrUser == 1) {
                    Prev2X = X;
                    Prev2Y = Y;
                    Temp1Path = new Path();
                    Temp1Path.addPath(pathU1);
                    TempCPath = new Path();
                    TempCPath.addPath(pathU2);
                    PosX = new Vector<>();
                    PosY = new Vector<>();
                }
                PrevX = X;
                PrevY = Y;

                Sqr.get(i).LeftComp = true;
                Sqr.get(i).nSidesCompleted++;
                if((i % (GridSide-1) != 0))
                {

                    Sqr.get(i-1).RightComp = true;
                    Sqr.get(i-1).nSidesCompleted++;
                    if(CheckFinish(i-1)) {
                        //getCurrPath().addPath(path);
                        AlCompleted = 1;
                    }

                }
                path.moveTo(temp.Ax - Radius,temp.Ay);
                path.addRect(temp.Ax - Radius+3, temp.Ay, temp.Ax + Radius -3, temp.Dy, Path.Direction.CW);

                if(!CheckFinish(i) && AlCompleted == 0)
                {
                    getCurrPath().addPath(path);
                    ChangeUser();

                }

                else {
                    getCurrPath().addPath(path);
                    PrevUser = CurrUser;

                }
                break;
            }

            if(CheckRect(temp.Ax,temp.Dx,temp.Ay - Radius ,temp.Ay + Radius ,X,Y) && (!temp.TopComp) )
            {
                PosX.add(X);
                PosY.add(Y);
                if(CurrUser == 1) {
                    Prev2X = X;
                    Prev2Y = Y;
                    Temp1Path = new Path();
                    Temp1Path.addPath(pathU1);
                    TempCPath = new Path();
                    TempCPath.addPath(pathU2);
                    PosX = new Vector<>();
                    PosY = new Vector<>();
                }
                PrevX = X;
                PrevY = Y;
                Sqr.get(i).TopComp = true;
                Sqr.get(i).nSidesCompleted++;
                if(i >= GridSide -1)
                {
                    Sqr.get(i - GridSide + 1).BottomComp = true;
                    Sqr.get(i - GridSide  +1).nSidesCompleted++;
                    if(CheckFinish(i- GridSide +1)){
                    //getCurrPath().addPath(path);
                    AlCompleted = 1;
                }
                    //Log.i("TAG",Sqr.get(i- GridSide + 1).toString());
                }
                path.moveTo(temp.Ax,temp.Ay-Radius);
                path.addRect(temp.Ax,temp.Ay - Radius+3,temp.Dx,temp.Ay + Radius-3, Path.Direction.CW);

                if(!CheckFinish(i) && AlCompleted == 0)
                {
                    getCurrPath().addPath(path);
                    Log.i("TAG",i + Sqr.get(i).toString());
                    ChangeUser();
                    Log.i("TAG"," not finish");
                }
                else {
                    getCurrPath().addPath(path);
                    PrevUser = CurrUser;
                    Log.i("TAG", " finished");
                }
                break;
            }

            if( (i+1) %(GridSide - 1) == 0)
            {
                PosX.add(X);
                PosY.add(Y);
                if(CheckRect(temp.Dx - Radius,temp.Dx + Radius,temp.Ay,temp.Dy,X,Y) && !(temp.RightComp))
                {
                    if(CurrUser == 1) {
                        Prev2X = X;
                        Prev2Y = Y;
                        Temp1Path = new Path();
                        Temp1Path.addPath(pathU1);
                        TempCPath = new Path();
                        TempCPath.addPath(pathU2);
                        PosX = new Vector<>();
                        PosY = new Vector<>();
                    }

                    PrevX = X;
                    PrevY = Y;
                    temp.RightComp = true;
                    path.moveTo(temp.Dx - Radius,temp.Ay);
                    path.addRect(temp.Dx - Radius+3,temp.Ay,temp.Dx + Radius-3,temp.Dy, Path.Direction.CW);
                    Sqr.get(i).nSidesCompleted++;

                    if(!CheckFinish(i))
                    {
                        getCurrPath().addPath(path);
                        Log.i("TAG",i + Sqr.get(i).toString());
                        ChangeUser();
                        Log.i("TAG"," not finished");
                    }
                    else {
                        PrevUser = CurrUser;
                        getCurrPath().addPath(path);
                        Log.i("TAG", " finished");
                    }
                    break;
                }
            }

            if( i >= (GridSide - 2) * (GridSide -1))
            {
                PosX.add(X);
                PosY.add(Y);
                if(CheckRect(temp.Ax,temp.Dx,temp.Dy - Radius,temp.Dy+Radius,X,Y) && !(temp.BottomComp))
                {
                    if(CurrUser == 1) {
                        Prev2X = X;
                        Prev2Y = Y;
                        Temp1Path = new Path();
                        Temp1Path.addPath(pathU1);
                        TempCPath = new Path();
                        TempCPath.addPath(pathU2);
                        PosX = new Vector<>();
                        PosY = new Vector<>();
                    }
                    PrevX = X;
                    PrevY = Y;
                    temp.BottomComp = true;
                    path.moveTo(temp.Ax,temp.Dy - Radius);
                    path.addRect(temp.Ax,temp.Dy - Radius+3,temp.Dx,temp.Dy + Radius-3, Path.Direction.CW);
                    Sqr.get(i).nSidesCompleted++;

                    if(!CheckFinish(i))
                    {
                        getCurrPath().addPath(path);
                        Log.i("TAG",i + Sqr.get(i).toString());
                        ChangeUser();
                        Log.i("TAG"," not finished");
                    }
                    else {
                        PrevUser = CurrUser;
                        getCurrPath().addPath(path);
                        Log.i("TAG", "  finished");
                    }
                        break;
                }
            }

        }

        if ((Score1 + Score2 + Score3 + Score4) == (GridSide - 1) * (GridSide - 1))
            IsGameOver = true;

        if(IsCompPlaying && CurrUser == 2 && Difficulty == 0 && !IsGameOver)
            ComputerTurn();


    }


    public boolean CheckRect(float Rx1,float Rx2,float Ry1, float Ry2,float X, float Y)
    {
        if( X > Rx1 && X < Rx2)
            if(Y > Ry1 && Y < Ry2)
                return true;

        return false;
    }


    public boolean CheckFinish(int i)
    {
        if(Sqr.get(i).nSidesCompleted > 3 && !(Sqr.get(i).isCompleted))
        {

            Sqr.get(i).isCompleted = true;
            path.moveTo(Sqr.get(i).Ax,Sqr.get(i).Ay);
            path.addRect(Sqr.get(i).Ax + Radius +10,Sqr.get(i).Ay + Radius + 10,Sqr.get(i).Dx - Radius - 10,Sqr.get(i).Dy - Radius - 10, Path.Direction.CW);

            switch (CurrUser)
            {
                case 1 : Score1++;
                         break;
                case 2 : Score2++;
                         break;
                case 3 : Score3++;
                    break;
                case 4 : Score4++;
                    break;
            }
            return true;
        }

        if(Sqr.get(i).isCompleted)
            return true;

        return false;
    }


    public Path getCurrPath() {
        TempPath = new Path();
        switch (CurrUser) {
            case 1:
                TempPath.addPath(pathU1);
                return pathU1;

            case 2:
                TempPath.addPath(pathU2);
                return pathU2;

            case 3:
                TempPath.addPath(pathU3);
                return  pathU3;

            case 4:
                TempPath.addPath(pathU4);
                return  pathU4;

            default:
                TempPath.addPath(pathU1);
                return pathU1;
        }
    }

    public void ChangeUser(){
        switch (CurrUser) {
            case 1:
                PrevUser = 1;
                CurrUser++;
                break;

            case 2:
                PrevUser =2;
                if(nPlayers > 2)
                    CurrUser++;
                else
                    CurrUser = 1;
                break;

            case 3:
                PrevUser =  3;
                if(nPlayers > 3)
                    CurrUser++;
                else
                    CurrUser = 1;
                break;

            case 4:
                PrevUser = 4;
                CurrUser = 1;
                break;

            default:
                Log.i("TAG","ERRORRR IN ChangeUser");
        }
    }

    public void ComputerTurn() {
        Log.i("TAG"," pos = " + pos);
        if (!IsGameOver) {
            if (Difficulty == 1 && !IsGameOver) {

                for (int i = 0; i < Sqr.size(); i++) {

                    if (Sqr.get(i).nSidesCompleted == 3) {

                        Log.i("TAG", " iNSIDE THE DIFFUICULTY");
                        if (!(Sqr.get(i).LeftComp))
                            SideCheck(Sqr.get(i).Ax, (Sqr.get(i).Ay + Sqr.get(i).Dy) / 2);

                        else if (!(Sqr.get(i).TopComp))
                            SideCheck((Sqr.get(i).Ax + Sqr.get(i).Dx) / 2, Sqr.get(i).Ay);
                        else if (!(Sqr.get(i).RightComp))
                            SideCheck(Sqr.get(i).Dx, (Sqr.get(i).Ay + Sqr.get(i).Dy) / 2);
                        else
                            SideCheck((Sqr.get(i).Ax + Sqr.get(i).Dx) / 2, Sqr.get(i).Dy);

                        pos.removeElement(i);
                    }
                }
            }
            if(!IsGameOver) {

                Random random = new Random();
                int j = random.nextInt(pos.size());
               // Log.i("TAG"," " +i);
                int i = pos.get(j);

                if (random.nextBoolean()) {
                    if (!(Sqr.get(i).LeftComp))
                        SideCheck(Sqr.get(i).Ax, (Sqr.get(i).Ay + Sqr.get(i).Dy) / 2);

                    else if (!(Sqr.get(i).RightComp))
                        SideCheck(Sqr.get(i).Dx, (Sqr.get(i).Ay + Sqr.get(i).Dy) / 2);

                    else if (!(Sqr.get(i).TopComp))
                        SideCheck((Sqr.get(i).Ax + Sqr.get(i).Dx) / 2, Sqr.get(i).Ay);

                    else
                        SideCheck((Sqr.get(i).Ax + Sqr.get(i).Dx) / 2, Sqr.get(i).Dy);
                }

                else {
                    if (!(Sqr.get(i).TopComp))
                        SideCheck((Sqr.get(i).Ax + Sqr.get(i).Dx) / 2, Sqr.get(i).Ay);

                    else if (!(Sqr.get(i).BottomComp))
                        SideCheck((Sqr.get(i).Ax + Sqr.get(i).Dx) / 2, Sqr.get(i).Dy);

                    else if (!(Sqr.get(i).RightComp))
                        SideCheck(Sqr.get(i).Dx, (Sqr.get(i).Ay + Sqr.get(i).Dy) / 2);

                    else
                        SideCheck(Sqr.get(i).Ax, (Sqr.get(i).Ay + Sqr.get(i).Dy) / 2);

                }

                if(Sqr.get(i).isCompleted)
                    pos.removeElement(i);
            }
        }
    }


    public void Undo(float X,float Y)
    {
        if(X < width && X > width-100 && Y < 330 && Y>230)
        {
            CurrUser = PrevUser;
            SideUndo(PrevX,PrevY);

            switch (PrevUser)
            {
                case 1: pathU1 = new Path();
                        pathU1.addPath(TempPath);
                        break;

                case 2: pathU2 = new Path();
                        pathU2.addPath(TempPath);
                        break;

                case 3: pathU3 = new Path();
                        pathU3.addPath(TempPath);
                        break;

                case 4: pathU4 = new Path();
                        pathU4.addPath(TempPath);
                        break;
            }

        }
    }

    public void UndoComp(float X,float Y)
    {
        if(X < width && X > width-100 && Y < 330 && Y>230)
        {


            CurrUser=2;
            for(int i =0; i < PosX.size();i++)
                SideUndo(PosX.get(PosX.size() -i -1),PosY.get(PosX.size()-i -1));

            pathU2 = new Path();
            pathU2.addPath(TempCPath);
            CurrUser = 1;
            SideUndo(Prev2X,Prev2Y);

            pathU1 = new Path();
            pathU1.addPath(Temp1Path);


        }
    }


    public void SideUndo(float X,float Y)
    {

        int i =0;
        for(; i < Sqr.size() ; i++) {
            square temp = Sqr.get(i);

            if (CheckRect(temp.Ax - Radius, temp.Ax + Radius, temp.Ay, temp.Dy, X, Y)) {
                if(Sqr.get(i).nSidesCompleted == 4)
                    ScoreDecrement();
                if(Sqr.get(i).LeftComp)
                Sqr.get(i).nSidesCompleted--;

                Sqr.get(i).LeftComp = false;
                Sqr.get(i).isCompleted = false;
                if((i % (GridSide-1) != 0))
                {
                    if(Sqr.get(i-1).nSidesCompleted  == 4)
                        ScoreDecrement();
                    if(Sqr.get(i-1).RightComp)
                        Sqr.get(i-1).nSidesCompleted--;

                    Sqr.get(i-1).RightComp = false;
                    Sqr.get(i-1).isCompleted = false;
                }
                break;
            }

            if(CheckRect(temp.Ax,temp.Dx,temp.Ay - Radius,temp.Ay + Radius,X,Y))
            {
                if(Sqr.get(i).nSidesCompleted == 4)
                    ScoreDecrement();
                if(Sqr.get(i).TopComp)
                    Sqr.get(i).nSidesCompleted--;
                Sqr.get(i).TopComp = false;
                Sqr.get(i).isCompleted = false;
                if(i >= GridSide -1)
                {
                    if(Sqr.get(i-GridSide + 1).nSidesCompleted  == 4)
                        ScoreDecrement();
                    if( Sqr.get(i - GridSide + 1).BottomComp)
                        Sqr.get(i - GridSide  +1).nSidesCompleted--;

                    Sqr.get(i - GridSide + 1).BottomComp = false;
                    Sqr.get(i- GridSide + 1).isCompleted = false;
                    }
                break;
            }

            if( (i+1) %(GridSide - 1) == 0)
            {
                if(CheckRect(temp.Dx - Radius,temp.Dx + Radius,temp.Ay,temp.Dy,X,Y))
                {
                    if(Sqr.get(i).nSidesCompleted == 4)
                        ScoreDecrement();
                    if(Sqr.get(i).RightComp)
                        Sqr.get(i).nSidesCompleted--;
                    Sqr.get(i).RightComp = false;
                    Sqr.get(i).isCompleted = false;
                    break;
                }
            }

            if( i >= (GridSide - 2) * (GridSide -1))
            {
                if(CheckRect(temp.Ax,temp.Dx,temp.Dy - Radius,temp.Dy+Radius,X,Y))
                {
                    if(Sqr.get(i).nSidesCompleted == 4)
                        ScoreDecrement();
                    if(Sqr.get(i).BottomComp)
                        Sqr.get(i).nSidesCompleted--;
                    Sqr.get(i).BottomComp = false;
                    Sqr.get(i).isCompleted = false;
                    break;
                }
            }

        }
    }

    public void ScoreDecrement()
    {
        switch(CurrUser)
        {
            case 1: Score1--;
                    break;
            case 2: Score2--;
                    break;
            case 3: Score3--;
                    break;
            case 4: Score4--;
                break;
        }
    }

}