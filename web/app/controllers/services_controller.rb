class ServicesController < ApplicationController
	before_filter :signed_in_user
	before_action :set_vehicle, only: [:index]
	before_action :set_user, only: [:index]
	before_action :set_service, only: [:show, :edit, :update, :destroy]
	before_filter :is_valid_user

	# GET /services
	# GET /services.json
	def index
		@services = services
	end
	
	# GET /services/graphic
	def graphic
		@services = services
	end

	# GET /services/1
	# GET /services/1.json
	def show
		@vehicle_service = VehicleService.new
		@location_service = LocationService.new
		@service_user = ServiceUser.new
	end

	# GET /services/new
	def new
		@service = Service.new
	end

	# GET /services/1/edit
	def edit
	end

	# POST /services
	# POST /services.json
	def create
		@service = Service.new(service_params)

		respond_to do |format|
			if @service.save
				format.html { redirect_to @service, notice: I18n.t(:service_created) }
				format.json { render action: 'show', status: :created, location: @service }
			else
				format.html { render action: 'new' }
				format.json { render json: @service.errors, status: :unprocessable_entity }
			end
		end
	end

	# PATCH/PUT /services/1
	# PATCH/PUT /services/1.json
	def update
		respond_to do |format|
			if @service.update(service_params)
				format.html { redirect_to @service, notice: I18n.t(:service_updated) }
				format.json { head :no_content }
			else
				format.html { render action: 'edit' }
				format.json { render json: @service.errors, status: :unprocessable_entity }
			end
		end
	end

	# DELETE /services/1
	# DELETE /services/1.json
	def destroy
		@service.destroy
		respond_to do |format|
			format.html { redirect_to services_url }
			format.json { head :no_content }
		end
	end

	private
		def set_vehicle
			@vehicle = (params[:vehicle_id]) ? Vehicle.find(params[:vehicle_id]) : nil
		end
		
		def set_user
			@user = (params[:user_id]) ? User.find(params[:user_id]) : nil
		end
		
		def services
			(@vehicle) ? @vehicle.services : ((@user) ? @user.services : Service.all )
		end
		
		# Use callbacks to share common setup or constraints between actions.
		def set_service
			@service = Service.find(params[:id]) || not_found
		end

		# Never trust parameters from the scary internet, only allow the white list through.
		def service_params
			params.require(:service).permit(:name, :description, :archived, :assembly_id, :base_time, :start_time, :end_time, :code)
		end
		
		def is_valid_user
			redirect_to root_url unless current_user && current_user.allowed_to?(:manage_services)
		end
end
