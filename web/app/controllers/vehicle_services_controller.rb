class VehicleServicesController < ApplicationController
	before_filter :signed_in_user
	before_filter :is_valid_user, only: [:new, :create, :edit, :update, :destroy]
	
	def create
		vehicle_service = VehicleService.new(vehicle_service_params)
		if vehicle_service.save
			redirect_to vehicle_service.service, notice: I18n.t(:vehicle_assigned_to_service)
		else
			redirect_to vehicle_service.service
		end
	end
	
	def update
		vehicle_service = VehicleService.find(params[:id])
		vehicle_service.update(vehicle_service_params)
		redirect_to(vehicle_service.service)
	end
	
	def destroy
		vehicle_service = VehicleService.find(params[:id])
		service = vehicle_service.service
		vehicle_service.destroy
		redirect_to(service)
	end
	
	private
		def vehicle_service_params
			params.require(:vehicle_service).permit(:vehicle_id, :service_id, :doc, :due, :tes, :ci, :asi, :btp, :b1, :acu, :per)
		end
		
		def is_valid_user
			redirect_to root_url unless current_user && current_user.allowed_to?(:assign_vehicle_to_service)
		end
end
