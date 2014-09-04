json.array!(@services) do |service|
  json.extract! service, :id, :name, :description, :assembly_id, :base_time, :start_time, :end_time, :code
  json.url service_url(service, format: :json)
end
