module ServicesHelper
	#Service: Service for which the vehicles are requested
	#Assignment: Services for which the vehicle is already assigned
	def get_available_vehicles(service)
		availables = []
		Vehicle.where(operative: true).each do |vehicle|
			#If the service already has the vehicle, ignore
			unless service.vehicles.include?(vehicle)
				is_available = true
				vehicle.services.each do |assignment|
					#If the assigned services match in time with the service wanted, mark it as not available
					if (service.base_time < assignment.end_time && service.end_time > assignment.base_time)
						is_available = false
					end
				end
				if is_available
					tag = "#{vehicle.indicative}, #{vehicle.license}"
					availables << [tag, vehicle.id] 
				end
			end
		end
		availables
	end
	
	def get_available_locations(service)
		locations = []
		Location.active_locations.each do |location|
			locations << [location.name, location.id] unless service.locations.include?(location)
		end
		locations
	end
	
	def get_available_users(service)
		availables = []
		User.where(active: true).each do |user|
			unless service.users.include?(user)
				is_available = true
				user.services.each do |assignment|
					is_available = false if (service.base_time < assignment.end_time && service.end_time > assignment.base_time)
				end
			end
			if is_available
				tag = "#{user.name} #{user.surname}"
				availables << [tag, user.id] 
			end
		end
		availables
	end
end
