json.array!(@locations) do |location|
	json.extract! location, :id, :name, :description, :address, :phone, :latitude, :longitude, :location_type, :active, :updated_at
end