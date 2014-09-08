class VehiclePositionsController < ApplicationController
	def index
		positions = VehiclePosition.select(:vehicle_id).distinct
		@vehicles = []
		positions.each do |position|
			@vehicles << position.vehicle.vehicle_positions.last
		end
		@vehicles
	end

	def create
		position = VehiclePosition.new(vehicle_position_params)
		position.indicative = position.vehicle.indicative
		respond_to do |format|
			if position.save
				format.json { head :created }
			else
				format.json { head :bad_request }
			end
		end
	end
	
	private
	def vehicle_positions_params
		params.require(:vehicle_id)
		params.require(:latitude)
		params.require(:longitude)
	end
end
