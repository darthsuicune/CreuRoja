json.array!(@issues) do |issue|
  json.extract! issue, :id, :status, :severity, :short_description, :long_description, :component
  json.url issue_url(issue, format: :json)
end
