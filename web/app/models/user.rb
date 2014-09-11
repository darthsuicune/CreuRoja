class User < ActiveRecord::Base
	default_scope { order(name: :asc, surname: :asc) }
	has_secure_password

	has_many :sessions, dependent: :destroy
	has_many :user_types, dependent: :destroy
	has_many :location_users, dependent: :destroy
	has_many :assemblies, through: :location_users, source: :location
	has_many :service_users
	has_many :services, through: :service_users
	
	accepts_nested_attributes_for :user_types, allow_destroy: true
	
	before_save { email.downcase!
	              role.downcase unless role.nil? }
	
	before_validation :defaults

	VALID_EMAIL_FORMAT = /\A[\w+\-.]+@[a-z\d\-.]+\.[a-z]+\z/i
	validates :email, presence: true, format: { with: VALID_EMAIL_FORMAT }, 
			uniqueness: { case_sensitive: false }
	validates :name, presence: true, length: { maximum: 60 }
	validates :surname, presence: true, length: { maximum: 60 }
	validates :password, length: { minimum: 6 }, on: :update, allow_blank: true
	validates :password_confirmation, length: { minimum: 6 }, on: :update, allow_blank: true
  
	after_validation { self.errors.messages.delete(:password_digest) }
	
	def get_visible_locations
		Location.active_locations
	end

	def allowed_to?(action)
		return false if active == false
		return true if role == "admin"
		case action
		when :see_map
			role == "volunteer" || role == "technician"
		when :see_own_profile
			role == "volunteer" || role == "technician"
		when :see_user_list
			role == "technician"
		when :see_location_list
			role == "technician"
		when :see_vehicle_list
			role == "technician"
		when :see_service_list
			role == "technician"
		when :add_to_own_assembly
			role == "technician"
		when :add_to_any_assembly
			false
		when :manage_users
			role == "technician"
		when :assign_vehicle_to_service
			role == "technician"
		when :destroy_users
			role == "technician"
		when :manage_admin_users
			false
		when :manage_issues
			role == "technician"
		when :manage_locations
			role == "technician"
		when :manage_services
			role == "technician"
		when :manage_vehicles
			role == "technician"
		when :edit_user
			role == "technician"
		else
			false
		end
	end
	
	def create_reset_password_token(time = Time.now)
		self.resettoken = SecureRandom.urlsafe_base64
		self.resettime = time
		if self.save!
			UserMailer.password_reset(self).deliver
		end
	end
	
	def reset_password(password)
		self.password = password
		self.password_confirmation = password
		self.resettoken = nil
		self.save
	end

	def create_session_token
		self.sessions.create(token: SecureRandom.urlsafe_base64)
	end
	
	def translated_role
		case role
		when "admin"
			I18n.t(:role_admin)
		when "technician"
			I18n.t(:role_technician)
		else #when "volunteer"
			I18n.t(:role_volunteer)
		end
	end
	
	def self.positions
		[[I18n.t(:position_b1), "b1"], [I18n.t(:position_per), "per"]]
	end
	
	private
		def defaults
			self.language ||= "ca"
			self.role ||= "volunteer"
			self.phone ||= 0
			pass = SecureRandom.urlsafe_base64
			self.password ||= pass
			self.password_confirmation ||= pass
		end
end
