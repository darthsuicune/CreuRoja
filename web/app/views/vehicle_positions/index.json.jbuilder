json.array!(@vehicles) do |vehicle|
	json.extract! vehicle, :id, :indicative, :latitude, :longitude
end
