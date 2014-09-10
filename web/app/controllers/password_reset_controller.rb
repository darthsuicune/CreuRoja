class PasswordResetController < ApplicationController
	def new
	end
	
	def create
		user = User.find_by_email(params[:user][:email])
		if user && user.create_reset_password_token
			#TODO: Uncomment once Rails goes into production
			#redirect_to login_path, :notice => I18n.t(:password_recovery_email_sent)
			redirect_to email_sent_url
		else
			@email = params[:user][:email]
			flash.now[:notice] = I18n.t(:error_invalid_email)
			render 'new'
		end
	end
	
	def edit
		@user = User.find_by_resettoken!(params[:id])
		if @user.nil? || @user.resettime < 4.hours.ago
			redirect_to root_url 
			@user.errors << I18n.t(:error_invalid_token) unless @user.nil?
		end
		flash.now[:notice] = I18n.t(:set_your_new_password)
	end
	
	def update
		@user = User.find_by_resettoken!(params[:id])
		if @user && params[:user][:password] && params[:user][:password_confirmation] && 
				params[:user][:password] == params[:user][:password_confirmation] && 
				params[:user][:password].length >= 6 && 
				params[:user][:password_confirmation].length >= 6 && 
				@user.resettime >= 4.hours.ago && @user.reset_password(params[:user][:password])
			#TODO: Uncomment once Rails gets into production
			#sign_in @user
			#redirect_to @user
			redirect_to root_url
		else
			if @user
				@errors = []
				@errors << I18n.t(:password_must_be_set) unless params[:user][:password]
				@errors << I18n.t(:password_confirmation_must_be_set) unless params[:user][:password_confirmation]
				@errors << I18n.t(:password_and_confirmation_must_match) unless params[:user][:password] == params[:user][:password_confirmation]
				@errors << I18n.t(:password_and_confirmation_must_be_longer) if params[:user][:password].length < 6 || params[:user][:password_confirmation].length < 6
				@errors << I18n.t(:reset_token_has_expired) unless @user.resettime >= 4.hours.ago
				render 'edit'
			else
				redirect_to root_url
			end
		end
	end
end
