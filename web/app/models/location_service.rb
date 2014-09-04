class LocationService < ActiveRecord::Base
	belongs_to :location
	belongs_to :service
	
	validates :location_id, presence: true
	validates :service_id, presence: true
end
