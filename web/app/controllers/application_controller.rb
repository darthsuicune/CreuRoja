class ApplicationController < ActionController::Base
	# Prevent CSRF attacks by raising an exception.
	# For APIs, you may want to use :null_session instead.
	# Through https://coderwall.com/p/8z7z3a
	protect_from_forgery with: :null_session, if: Proc.new { |c| c.request.format == 'application/json' }
	before_action :log
	before_action :set_locale
	
	include SessionsHelper
	
	def default_url_options(options={})
		if params && params[:locale]
			{ locale: I18n.locale }
		else
			Rails.application.routes.default_url_options
		end
	end
	
	def not_found
		raise ActiveRecord::RecordNotFound.new('Not Found')
	end

	private
		def log
			unless controller_name == "vehicle_positions"
				if (action_name == "create" || action_name == "update" || action_name == "destroy")
					user_id = (current_user) ? current_user.id : 0
					Log.log(user_id, controller_name, action_name, request.remote_ip)
				end
			end
		end
		
		def set_locale
			I18n.locale = params[:locale] || (current_user) ? current_user.language : I18n.default_locale
		end
end
