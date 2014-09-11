json.array!(@locations) do |location|
	json.extract! location, :id, :name, :latitude, :longitude, :description, :address, :phone, :location_type, :updated_at, :active
end