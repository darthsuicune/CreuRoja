class LocationsController < ApplicationController
	before_filter :signed_in_user
	before_filter :is_valid_user, except: [:index, :map]
	before_action :set_location, only: [:show, :edit, :update, :destroy]

	# GET /locations
	# GET /locations.json
	def index
		#This next piece has a retard as its ideological author. Don't blame my hands for it.
		respond_to do |format|
			format.html {
				redirect_to root_url unless current_user.allowed_to?(:manage_locations)
				@locations = Location.all 
			}
			format.json {
				@locations = if params[:updated_at]
					Location.all.where("updated_at > ?", params[:updated_at])
				else
					Location.active_locations
				end
			}
		end
	end

	# GET /locations/1
	# GET /locations/1.json
	def show
	end
	
	def map
		@locations = current_user.get_visible_locations
	end

	# GET /locations/new
	def new
		@location = Location.new
	end

	# GET /locations/1/edit
	def edit
	end

	# POST /locations
	# POST /locations.json
	def create
		#Change ',' characters for '.' so they work
		replace_commas 
		@location = Location.new(location_params)
		respond_to do |format|
			if @location.save
				format.html { redirect_to @location, notice: I18n.t(:location_created) }
				format.json { render action: 'show', status: :created, location: @location }
			else
				format.html { render action: 'new' }
				format.json { render json: @location.errors, status: :unprocessable_entity }
			end
		end
	end

	# PATCH/PUT /locations/1
	# PATCH/PUT /locations/1.json
	def update
		#Change ',' characters for '.' so they work
		replace_commas 
		respond_to do |format|
			if @location.update(location_params)
				format.html { redirect_to locations_path, notice: I18n.t(:location_updated) }
				format.json { head :no_content }
			else
				format.html { render action: 'edit' }
				format.json { render json: @location.errors, status: :unprocessable_entity }
			end
		end
	end

	# DELETE /locations/1
	# DELETE /locations/1.json
	def destroy
		@location.destroy
		respond_to do |format|
			format.html { redirect_to locations_url }
			format.json { head :no_content }
		end
	end

	private
		def replace_commas
			params[:location][:latitude].sub! ",", "." if params[:location][:latitude]
			params[:location][:longitude].sub! ",", "." if params[:location][:longitude]
		end
		# Use callbacks to share common setup or constraints between actions.
		def set_location
			@location = Location.find(params[:id]) || not_found
		end

		# Never trust parameters from the scary internet, only allow the white list through.
		def location_params
			params.require(:location).permit(:name, :description, :address, :phone, :latitude, :longitude, :location_type, :active)
		end
		
		def is_valid_user
			redirect_to root_url unless current_user && current_user.allowed_to?(:manage_locations)
		end
end
