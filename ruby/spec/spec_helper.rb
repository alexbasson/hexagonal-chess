Dir[File.join(__dir__, '..', '*', 'lib')].each do |path|
  $LOAD_PATH.unshift(path) unless $LOAD_PATH.include?(path)
end

RSpec.configure do |config|
  config.expect_with :rspec do |expectations|
    expectations.include_chain_clauses_in_custom_matcher_descriptions = true
  end

  config.mock_with :rspec do |mocks|
    mocks.verify_partial_doubles = true
    mocks.verify_doubled_constant_names = true
  end

  config.shared_context_metadata_behavior = :apply_to_host_groups
end
