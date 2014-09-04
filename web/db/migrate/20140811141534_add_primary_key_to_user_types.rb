class AddPrimaryKeyToUserTypes < ActiveRecord::Migration
	def change
		add_column :user_types, :id, :primary_key
	end
end
