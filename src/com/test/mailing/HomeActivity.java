package com.test.mailing;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnTouchListener {

		Button btn;
		EditText et_conntact;
		EditText et_subject;
		EditText et_message;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			btn = (Button)PlaceholderFragment.this.getView().findViewById(R.id.btn_send);
			
			et_conntact = (EditText)PlaceholderFragment.this.getView().findViewById(R.id.et_contact);
			et_subject = (EditText)PlaceholderFragment.this.getView().findViewById(R.id.et_subject);
			et_message = (EditText)PlaceholderFragment.this.getView().findViewById(R.id.et_content);
			
			btn.setOnTouchListener(this);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(btn.getId() == v.getId()){
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					String strContact = et_conntact.getText().toString();
					String strsubject = et_subject.getText().toString();
					String strMessage = et_message.getText().toString();
					
					if(strContact.equals("")){
						Toast.makeText(this.getActivity(), "Falto Destinatario", Toast.LENGTH_SHORT).show();
						return true;
					}
					if(strsubject.equals("")){
						Toast.makeText(this.getActivity(), "Falto Asunto.", Toast.LENGTH_SHORT).show();
						return true;
					}
					if(strMessage.equals("")){
						Toast.makeText(this.getActivity(), "Mensaje vacio.", Toast.LENGTH_SHORT).show();
					}
					
					performSendMail(strContact, strsubject, strMessage);
					return true;
				}
			}
			return false;
		}
		
		private void performSendMail(String email, String subject, String messageBody){
			Log.d("Hello", "World");
			Session session = createSessionObject();
			
			Message msg = createMessage(email, subject, messageBody, session);
			new SendMailTask().execute(msg);
		}

		private Message createMessage(String email, String subject,
				String messageBody, Session session) {
			Message msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress("alexxsanchezm@gmail.com", "Alex Sanchez"));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
				msg.setSubject(subject);
				msg.setText(messageBody);
			
			} catch (UnsupportedEncodingException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return msg;
		}

		private Session createSessionObject() {
			Properties properties = new Properties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
			
			
			return Session.getInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication("alexxsanchezm@gmail.com", "albk:1989");
	            }
	        });
		}
	
		private class SendMailTask extends AsyncTask<Message, Void, Void>{

			@Override
			protected void onPreExecute(){
				super.onPreExecute();
				Log.d("Hello", "Sending Mail......");
			}
			
			@Override
			protected Void doInBackground(Message... msgs) {
				try {
					Transport.send(msgs[0]);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void aVoid){
				super.onPostExecute(aVoid);
				Log.d("Hello", "Mail Sent, I Guess.");
			}
			
		}
	
	}
	
	

}
