class Location < ActiveRecord::Base
	default_scope { order(location_type: :desc) }
	has_many :services
	has_many :location_users, dependent: :destroy
	has_many :users, through: :location_users
	has_many :location_services, dependent: :destroy
	has_many :services, through: :location_services
	has_many :service_users, dependent: :destroy
	
	before_validation :defaults
	
	validates :name, presence: true
	validates :address, presence: true
	validates :latitude, presence: true
	validates :longitude, presence: true
	validates :location_type, presence: true
	
	def self.assemblies
		Location.active_locations.where(location_type: "asamblea")
	end
	
	def self.active_locations
		Location.where(active: true)
	end
	
	def self.location_types
		Location.active_locations.select(:location_type).distinct
	end
	
	private
	def defaults
	end
end
