class VehiclePositionsController < ApplicationController
	before_filter :signed_in_user
	before_filter :is_valid_user
	
	def index
		positions = VehiclePosition.select(:vehicle_id).distinct
		@vehicles = []
		positions.each do |position|
			@vehicles << position.vehicle.vehicle_positions.last
		end
		@vehicles
	end

	def create
		respond_to do |format|
			format.html {
				head :unauthorized 
				redirect_to root_url
			}
			format.json {
				position = VehiclePosition.new(vehicle_positions_params)
				position.indicative = position.vehicle.indicative
				if position.save
					head :created 
				else
					head :bad_request 
				end
			}
		end
	end
	
	private
	def vehicle_positions_params
		params.require(:vehicle_position).permit(:vehicle_id, :latitude, :longitude)
	end
	
	def is_valid_user
		redirect_to root_url unless current_user && current_user.allowed_to?(:manage_vehicles)
	end
end
