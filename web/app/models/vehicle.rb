class Vehicle < ActiveRecord::Base
	default_scope { order(indicative: :asc) }
	
	has_many :vehicle_services, dependent: :destroy
	has_many :services, through: :vehicle_services
	has_many :vehicle_assemblies, dependent: :destroy, :source => :location
	has_many :assemblies, through: :vehicle_assemblies, :source => :location
	has_many :service_users, dependent: :destroy
	has_many :vehicle_positions, dependent: :destroy

	validates :indicative, presence: true
	validates :brand, presence: true
	validates :model, presence: true
	validates :license, presence: true
	validates :vehicle_type, presence: true
	validates :places, presence: true
	
	def to_s
		"#{indicative}"
	end
	
	def self.operative
		Vehicle.where(operative: true)
	end
	
	def translated_vehicle_type
		case vehicle_type
		when "alfa bravo"
			I18n.t(:vehicle_type_alfa_bravo)
		when "alfa mike"
			I18n.t(:vehicle_type_alfa_mike)
		when "mike"
			I18n.t(:vehicle_type_mike)
		when "romeo"
			I18n.t(:vehicle_type_romeo)
		when "tango"
			I18n.t(:vehicle_type_tango)
		else
			"dafuq"
		end
	end
	
	def driver(service)
		self.service_users.where(user_position: ["b1","btp","per"], service_id: service.id).first
	end

	protected
	def defaults
	end
end
