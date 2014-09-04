include ActionController::HttpAuthentication::Token::ControllerMethods
module SessionsHelper
	def sign_in(user)
		user.create_session_token
		cookies.permanent[:remember_token] = user.sessions.last.token #For HTML clients
		@session = user.sessions.last.token # For JSON clients
		self.current_user = user
	end
	
	def signed_in?
		!current_user.nil?
	end
	
	def sign_out
		@current_user = nil
		Session.find_by_token(cookies[:remember_token]).destroy!
		cookies.delete(:remember_token)
	end
	
	def current_user=(user)
		@current_user = user
	end
	
	def current_user
		token = cookies[:remember_token] || authenticate || params[:token] || nil
		session = Session.find_by_token(token) if token
		@current_user = session.user if session
	end
	
	def current_user?(user)
		user == current_user
	end
	
	def redirect_back_or(default)
		redirect_to(session[:return_to] || default)
		session.delete(:return_to)
	end
	
	def store_location
		session[:return_to] = request.url
	end
	
	def signed_in_user
		unless signed_in?
			respond_to do |format|
				format.html {
					store_location
					redirect_to signin_url
				}
				format.json {
					render file: "public/401.json", status: :unauthorized
				}
			end
		end
	end
	
	private
	def authenticate
		authenticate_with_http_token do |token,options|
			Session.exists?(token: token)
		end
	end
end
