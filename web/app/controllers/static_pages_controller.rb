class StaticPagesController < ApplicationController
	before_action :signed_in_user, only: [:map]
	before_filter :is_valid_user, only: [:map]

	def contact
	end
	
	def email_sent
	end
	
	def about
	end
	
	private
		def is_valid_user
			redirect_to root_url unless current_user && current_user.allowed_to?(:see_map)
		end
end
