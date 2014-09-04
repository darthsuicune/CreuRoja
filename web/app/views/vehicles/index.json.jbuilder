json.array!(@vehicles) do |vehicle|
  json.extract! vehicle, :id, :brand, :model, :license, :indicative, :vehicle_type, :places, :notes, :operative
  json.url vehicle_url(vehicle, format: :json)
end
