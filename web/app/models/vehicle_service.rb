class VehicleService < ActiveRecord::Base
	belongs_to :service
	belongs_to :vehicle
	
	before_validation :defaults
	
	validates :vehicle_id, presence: true
	validates :service_id, presence: true
	
	private
		def defaults
			self.doc ||= 0
			self.due ||= 0
			self.tes ||= 0
			self.ci  ||= 0
			self.asi ||= 0
			self.btp ||= 0
			self.b1  ||= 0
			self.acu ||= 0
			self.per ||= 0
		end
end
