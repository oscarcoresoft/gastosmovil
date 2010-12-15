package deeloco.android.gastos.Movil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TextBox extends AlertDialog implements OnClickListener{

    private static final String BUNDLE_TAGS = "tags";
    protected static final int DIALOG_ID_NO_FILE_MANAGER_AVAILABLE = 2;

    private Context mContext;
    private String valor;
    private String subtitulo="";
    private EditText mEditText;
    private TextView tvSubtitulo;

    public TextBox(Context context) {
        super(context);
        mContext = context;

        //this.setTitle(context.getText(R.string.tituloInputBox));
        //this.setMessage("Mensaje");
        this.setButton(context.getText(android.R.string.ok), this);
        this.setButton2(context.getText(android.R.string.cancel), (OnClickListener) null);
        //this.setIcon(android.R.drawable.ic_menu_more);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.textbox, null);
        mEditText = (EditText) view.findViewById(R.id.EditText01);
        //Log.d(TAG,"Valor inicial="+this.valor);
        mEditText.setText(this.valor);
        tvSubtitulo=(TextView) view.findViewById(R.id.subtitulo);
        tvSubtitulo.setText(this.subtitulo);
        setView(view);

    }


    public void onClick(DialogInterface dialog, int which) {
    	retornoValor();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putString(BUNDLE_TAGS, "");
        return state;
    }
    


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String tags = savedInstanceState.getString(BUNDLE_TAGS);
    }
    
    /**
     * Listener
     */
    
    public interface TextBoxListener {
    	
    	public void onOkClick(String valor); // User name is provided here.

    	//public void onCancelClick();
    	}
    
    private TextBoxListener tbListener;
    
    public void setTextBoxListener(TextBoxListener listener) {
    	tbListener = listener;
    }
    
    void retornoValor() {
        if (tbListener != null) {
                String valor = mEditText.getText().toString();
                tbListener.onOkClick(valor);
                this.valor=valor;
        }
    }

    
    /**
     * Set y Get
     * 
     */
    
    public void setValorInicial(String valorInicial){
    	this.valor=valorInicial;
    	mEditText.setText(this.valor);
    }
    
    public String getValor(){
    	return valor;
    }
    
    public void setSubtitulo(String subtitulo){
    	this.subtitulo=subtitulo;
    	tvSubtitulo.setText(this.subtitulo);
    }
    
    public String getSubtitulo(){
    	return this.subtitulo;
    }

}
