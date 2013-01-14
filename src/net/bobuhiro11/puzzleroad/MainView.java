package net.bobuhiro11.puzzleroad; 


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements
SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;
	private Thread thread;
	private long interval = 100;
	private Runnable runnable;
	private Handler handler = new Handler();

	private Paint paint;
	private Context context;
	
	public MainView(Context context) {
		super(context);
		
		this.context = context;
		
		//���\�[�X�̏���
		paint = new Paint();
		paint.setColor(Color.WHITE);



		// getHolder()���\�b�h��SurfaceHolder���擾�B����ɃR�[���o�b�N��o�^
		getHolder().addCallback(this);
		// �^�C�}�[�������J�n
		runnable = new Runnable() {
			public void run() {
				TimerEvent();
				handler.postDelayed(this, interval);
			}
		};
		handler.postDelayed(runnable, interval);
	}

	//�^�C�}�[�C�x���g(interval���ƂɌĂ΂��D)
	private void TimerEvent() {

	}

	// SurfaceView�������ɌĂяo�����
	public void surfaceCreated(SurfaceHolder holder) {
		this.holder = holder;
		thread = new Thread(this);
	}

	// SurfaceView�ύX���ɌĂяo�����
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// �X���b�h�X�^�[�g
		if (thread != null) {
			thread.start();
		}
	}

	// SurfaceView�j�����ɌĂяo�����
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	// �X���b�h�ɂ��SurfaceView�X�V����
	public void run() {
		while (thread != null) {
			// �X�V����
			update();
			// �`�揈��
			Canvas canvas = holder.lockCanvas();
			this.draw(canvas);
			holder.unlockCanvasAndPost(canvas);

		}
	}
	
	// �X�V����
	private void update(){
		
	}

	// �`�揈��
	@Override
	public void draw(Canvas canvas) {
		if(canvas==null){
			return;
		}
		//�`�揈��
		canvas.drawText("Hello Everyone!", 100, 100, paint);
	}

	// �^�b�`�C�x���g
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
}