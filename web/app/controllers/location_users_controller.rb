class LocationUsersController < ApplicationController
	before_filter :signed_in_user
	before_filter :is_valid_user
		
	def create
		@location_user = LocationUser.new(location_user_params)
		if @location_user.save
			redirect_to @location_user.user, notice: I18n.t(:user_assigned_to_assembly)
		else
			redirect_to @location_user.user
		end
	end
	
	def update
		location_user = LocationUser.find(params[:id])
		location_user.update(location_user_params)
		redirect_to(location_user.user)
	end
	
	def destroy
		location_user = LocationUser.find(params[:id])
		user = location_user.user
		location_user.destroy
		redirect_to(user)
	end
	
	private
		def location_user_params
			params.require(:location_user).permit(:location_id, :user_id)
		end
		def is_valid_user
			redirect_to root_url unless current_user && current_user.allowed_to?(:assign_users_to_assemblies)
		end
end
