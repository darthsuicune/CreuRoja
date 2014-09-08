class VehiclePosition < ActiveRecord::Base
	belongs_to :vehicle

	validates :vehicle_id, presence: true
	validates :indicative, presence: true
	validates :latitude, presence: true
	validates :longitude, presence: true
end
