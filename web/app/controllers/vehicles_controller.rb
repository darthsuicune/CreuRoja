class VehiclesController < ApplicationController
	before_filter :signed_in_user
	before_action :set_service, only: [:show, :index]
	before_action :set_vehicle, only: [:show, :edit, :update, :destroy]
	before_filter :is_valid_user

	# GET /vehicles
	# GET /vehicles.json
	def index
		@vehicles = vehicles
	end

	# GET /vehicles/1
	# GET /vehicles/1.json
	def show
	end

	# GET /vehicles/new
	def new
		@vehicle = Vehicle.new
	end

	# GET /vehicles/1/edit
	def edit
	end

	# POST /vehicles
	# POST /vehicles.json
	def create
		@vehicle = Vehicle.new(vehicle_params)

		respond_to do |format|
			if @vehicle.save
				format.html { redirect_to @vehicle, notice: I18n.t(:vehicle_created) }
				format.json { render action: 'show', status: :created, location: @vehicle }
			else
				format.html { render action: 'new' }
				format.json { render json: @vehicle.errors, status: :unprocessable_entity }
			end
		end
	end

	# PATCH/PUT /vehicles/1
	# PATCH/PUT /vehicles/1.json
	def update
		respond_to do |format|
			if @vehicle.update(vehicle_params)
				format.html { redirect_to @vehicle, notice: I18n.t(:vehicle_updated) }
				format.json { head :no_content }
			else
				format.html { render action: 'edit' }
				format.json { render json: @vehicle.errors, status: :unprocessable_entity }
			end
		end
	end

	# DELETE /vehicles/1
	# DELETE /vehicles/1.json
	def destroy
		@vehicle.destroy
		respond_to do |format|
			format.html { redirect_to vehicles_url }
			format.json { head :no_content }
		end
	end

	private
		def set_service
			@service = (params[:service_id]) ? Service.find(params[:service_id]) : nil
		end
		
		def vehicles
			(@service) ? @service.vehicles : Vehicle.all
		end
		
		# Use callbacks to share common setup or constraints between actions.
		def set_vehicle
			@vehicle = vehicles.find(params[:id]) || not_found
		end

		# Never trust parameters from the scary internet, only allow the white list through.
		def vehicle_params
			params.require(:vehicle).permit(:brand, :model, :license, :indicative, :vehicle_type, :places, :notes, :operative)
		end
		
		def is_valid_user
			redirect_to root_url unless current_user && current_user.allowed_to?(:manage_vehicles)
		end
end
