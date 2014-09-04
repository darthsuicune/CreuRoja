include ApplicationHelper

def sign_in(user)
	visit signin_path
	fill_in I18n.t(:form_user_email), with: user.email
	fill_in I18n.t(:form_user_password), with: user.password
	click_button I18n.t(:login_button)
	
	cookies[:remember_token] = user.sessions.last.token
end