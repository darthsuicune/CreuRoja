class VehicleAssembly < ActiveRecord::Base
	belongs_to :vehicle
	belongs_to :location
	
	validates :vehicle_id, presence: true
	validates :location_id, presence: true
end
