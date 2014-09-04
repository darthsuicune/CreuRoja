class CreateIssues < ActiveRecord::Migration
	def change
		create_table :issues do |t|
			t.string :status
			t.string :severity
			t.string :short_description
			t.string :long_description
			t.string :component

			t.timestamps
		end
	end
end
