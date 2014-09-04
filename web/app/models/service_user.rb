class ServiceUser < ActiveRecord::Base
	belongs_to :user
	belongs_to :service
	belongs_to :location
	belongs_to :vehicle
	
	before_validation :defaults
	
	validates :service_id, presence: true
	validates :user_id, presence: true
	validates :user_position, presence: true
	
	def name
		self.user.name
	end
	
	def surname
		self.user.surname
	end
	
	private
	def defaults
		self.location_id ||= service.first_location_id
	end
end
