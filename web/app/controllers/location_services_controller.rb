class LocationServicesController < ApplicationController
	before_filter :signed_in_user
	before_filter :is_valid_user, only: [:create, :update, :destroy]
	
	def create
		location_service = LocationService.new(location_service_params)
		if location_service.save
			redirect_to location_service.service, notice: I18n.t(:service_assigned_to_location)
		else
			redirect_to location_service.service
		end
	end

	def update
		location_service = LocationService.find(params[:id])
		location_service.update(location_service_params)
		redirect_to(location_service.service)
	end

	def destroy
		location_service = LocationService.find(params[:id])
		service = location_service.service
		location_service.destroy
		redirect_to(service)
	end
	
	private
	def location_service_params
		params.require(:location_service).permit(:location_id, :service_id, :doc, :due, :tes, :ci, :asi, :btp, :b1, :acu, :per)
	end
	
	def is_valid_user
		redirect_to root_url unless current_user && current_user.allowed_to?(:assign_service_to_location)
	end
end
